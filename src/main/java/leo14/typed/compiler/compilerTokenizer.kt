package leo14.typed.compiler

import leo.base.ifNotNull
import leo13.fold
import leo13.reverse
import leo14.*
import leo14.syntax.*
import leo14.typed.evaluator.Evaluator
import leo14.typed.process

val types = false

fun Processor<Syntax>.process(tokenReader: TokenReader): Processor<Syntax> =
	when (tokenReader) {
		is CompilerTokenReader -> process(tokenReader.compiler)
		is EvaluatorTokenReader -> process(tokenReader.evaluator)
	}

fun <T> Processor<Syntax>.process(compiler: Compiler<T>): Processor<Syntax> =
	when (compiler) {
		is ActionParserCompiler -> process(compiler.functionParser)
		is ArrowParserCompiler -> process(compiler.arrowParser)
		is ChoiceParserCompiler -> process(compiler.choiceParser)
		is CompiledParserCompiler -> processWithTypes(compiler.compiledParser)
		is DeleteParserCompiler -> process(compiler.deleteParser)
		is NothingParserCompiler -> process(compiler.nothingParser)
		is RememberParserCompiler -> process(compiler.memoryItemParser)
		is TypeParserCompiler -> process(compiler.typeParser)
		is MatchParserCompiler -> process(compiler.matchParser)
		is ScriptParserCompiler -> process(compiler.scriptParser)
		is LeonardoParserCompiler -> process(compiler.leonardoParser)
		is ForgetEverythingParserCompiler -> process(compiler.forgetEverythingParser)
		is ForgetEverythingEndParserCompiler -> process(compiler.forgetEverythingEndParser)
	}

fun <T> Processor<Syntax>.process(evaluator: Evaluator<T>): Processor<Syntax> =
	process(evaluator.compiler)

fun <T> Processor<Syntax>.process(parser: FunctionParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(Keyword.FUNCTION stringIn parser.parentCompiledParser.context.language)) of typeKeywordKind)
		.process(parser.function.takes, parser.parentCompiledParser.context.language)
		.process(token(begin(Keyword.DOES stringIn parser.parentCompiledParser.context.language)) of typeKeywordKind)
		.process(parser.function.does.type, parser.parentCompiledParser.context.language)
		.process(token(end) of typeKeywordKind)

fun <T> Processor<Syntax>.process(parser: ArrowParser<T>): Processor<Syntax> =
	this
		.ifNotNull(parser.parent.typeParser) { process(it) }
		.process(token(begin(Keyword.FUNCTION stringIn parser.parent.typeParser.language)) of typeKeywordKind)
		.process(parser.arrow, parser.parent.typeParser.language)

fun <T> Processor<Syntax>.process(parser: ChoiceParser<T>): Processor<Syntax> =
	this
		.ifNotNull(parser.parent?.typeParser) { process(it) }
		.process(token(begin(Keyword.CHOICE stringIn parser.language)) of typeKeywordKind)
		.fold(parser.choice.optionStack.reverse) { process(it, parser.language) }

fun <T> Processor<Syntax>.processWithTypes(compiledParser: CompiledParser<T>): Processor<Syntax> =
	if (types)
		this
			.process(compiledParser.compiled.typed.type, compiledParser.context.language)
			.process(token(begin("with")) of valueKeywordKind)
			.process(compiledParser)
	else process(compiledParser)

fun <T> Processor<Syntax>.process(compiledParser: CompiledParser<T>): Processor<Syntax> =
	this
		.ifNotNull(compiledParser.parent) { process(it) }
		.run {
			when (compiledParser.phase) {
				Phase.COMPILER -> process(compiledParser.compiled.typed.type, compiledParser.context.language)
				Phase.EVALUATOR -> syntaxProcess(compiledParser.decompile)
			}
		}

fun <T> Processor<Syntax>.process(parser: TypeParser<T>): Processor<Syntax> =
	this
		.ifNotNull(parser.parent) { process(it) }
		.ifNotNull(parser.beginner) { process(it) }
		.process(parser.type, parser.language)

fun <T> Processor<Syntax>.process(parser: DeleteParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(Keyword.DELETE stringIn parser.parentCompiledParser.context.language)) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parser: NothingParser<T>): Processor<Syntax> =
	process(parser.parentCompiledParser)
		.process(token(begin(Keyword.NOTHING stringIn parser.parentCompiledParser.context.language)) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parser: ScriptParser<T>): Processor<Syntax> =
	this
		.process(parser.parent)
		.syntaxProcess(parser.script)

fun <T> Processor<Syntax>.process(parser: LeonardoParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(Keyword.LEONARDO stringIn parser.parentCompiledParser.context.language)) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parser: ForgetEverythingParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(Keyword.FORGET stringIn parser.parentCompiledParser.context.language)) of valueKeywordKind)
		.process(token(begin(Keyword.EVERYTHING stringIn parser.parentCompiledParser.context.language)) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parser: ForgetEverythingEndParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(Keyword.FORGET stringIn parser.parentCompiledParser.context.language)) of valueKeywordKind)
		.process(token(begin(Keyword.EVERYTHING stringIn parser.parentCompiledParser.context.language)) of valueKeywordKind)
		.process(token(end) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parent: ScriptParserParent<T>): Processor<Syntax> =
	when (parent) {
		is FieldScriptParserParent -> this
			.process(parent.scriptParser)
			.process(token(begin(parent.name)) of valueKind)
		is MakeScriptParserParent -> this
			.process(parent.compiledParser)
			.process(token(begin(Keyword.MAKE stringIn parent.compiledParser.context.language)) of valueKind)
		is CommentScriptParserParent -> this
			.process(parent.compiler)
			.process(token(begin(Keyword.COMMENT stringIn defaultLanguage)) of commentKind)
		is CompiledScriptParserParent -> this
			.process(parent.compiledParser)
			.process(token(begin(Keyword.SCRIPT stringIn defaultLanguage)) of commentKind)
	}

fun <T> Processor<Syntax>.process(matchParser: MatchParser<T>): Processor<Syntax> =
	this
		.process(matchParser.parentCompiledParser)
		.process(token(begin(Keyword.MATCH stringIn matchParser.parentCompiledParser.context.language)) of valueKeywordKind)
		.fold(matchParser.caseLineStack.reverse) { process(it, matchParser.parentCompiledParser.context.language) }

fun <T> Processor<Syntax>.process(parent: CompiledParserParent<T>): Processor<Syntax> =
	when (parent) {
		is FunctionDoesParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(Keyword.FUNCTION stringIn parent.compiledParser.context.language)) of valueKeywordKind)
				.process(parent.type, parent.compiledParser.context.language)
				.process(token(begin(Keyword.DOES stringIn parent.compiledParser.context.language)) of valueKeywordKind)
		is FunctionGiveParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(Keyword.DO stringIn parent.compiledParser.context.language)) of valueKeywordKind)
		is FieldCompiledParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.name)) of valueKind)
		is RememberIsParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(Keyword.REMEMBER stringIn parent.compiledParser.context.language)) of valueKeywordKind)
				.process(parent.type, parent.compiledParser.context.language)
				.process(token(begin(Keyword.IS stringIn parent.compiledParser.context.language)) of valueKeywordKind)
		is RememberDoesParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(Keyword.REMEMBER stringIn parent.compiledParser.context.language)) of valueKeywordKind)
				.process(parent.type, parent.compiledParser.context.language)
				.process(token(begin(Keyword.DOES stringIn parent.compiledParser.context.language)) of valueKeywordKind)
		is GiveCompiledParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(Keyword.GIVE stringIn parent.compiledParser.context.language)) of valueKeywordKind)
		is UseCompiledParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(Keyword.USE stringIn parent.compiledParser.context.language)) of valueKeywordKind)
		is MatchParserParent ->
			this
				.process(parent.matchParser)
				.process(token(begin(parent.name)) of valueKeywordKind)
	}

fun <T> Processor<Syntax>.process(parent: TypeParserParent<T>): Processor<Syntax> =
	when (parent) {
		is LineTypeParserParent ->
			this
				.process(parent.typeParser)
				.process(token(begin(parent.name)) of typeKeywordKind)
		is ArrowGivingTypeParserParent ->
			this
				.process(token(begin(Keyword.FUNCTION stringIn parent.typeParser.language)) of typeKeywordKind)
				.process(parent.typeParser)
				.process(token(begin(Keyword.GIVING stringIn parent.typeParser.language)) of typeKeywordKind)
		is AsTypeParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(Keyword.AS stringIn parent.compiledParser.context.language)) of valueKeywordKind)
		is OptionTypeParserParent ->
			this
				.process(parent.choiceParser)
				.process(token(begin(parent.name)) of typeKind)
		is ForgetTypeParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(Keyword.FORGET stringIn parent.compiledParser.context.language)) of valueKeywordKind)
	}

fun <T> Processor<Syntax>.process(beginner: TypeBeginner<T>): Processor<Syntax> =
	when (beginner) {
		is FunctionGivesTypeBeginner ->
			this
				.process(beginner.compiledParser)
				.process(token(begin(Keyword.FUNCTION stringIn beginner.compiledParser.context.language)) of valueKeywordKind)
		is ArrowGivingTypeBeginner ->
			this
				.process(beginner.typeParser)
				.process(token(begin(Keyword.GIVING stringIn beginner.typeParser.language)) of valueKeywordKind)
		is RememberTypeBeginner ->
			this
				.process(beginner.compiledParser)
				.process(token(begin(Keyword.REMEMBER stringIn beginner.compiledParser.context.language)) of valueKeywordKind)
		is ForgetTypeBeginner -> this
	}

fun <T> Processor<Syntax>.process(parser: MemoryItemParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(Keyword.REMEMBER stringIn parser.parentCompiledParser.context.language)) of valueKeywordKind)
		.process(parser.memoryItem, parser.parentCompiledParser.context.language)

fun <T> Processor<Syntax>.process(
	memoryItem: MemoryItem<T>,
	language: Language): Processor<Syntax> =
	this
		.process(memoryItem.key.type, language)
		.process(token(begin(Keyword.DOES stringIn language)) of valueKeywordKind)
		.process(memoryItem.value.type, language)
		.process(token(end) of valueKeywordKind)
