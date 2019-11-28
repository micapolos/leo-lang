package leo14.typed.compiler

import leo.base.ifNotNull
import leo13.fold
import leo13.reverse
import leo14.*
import leo14.syntax.*
import leo14.typed.DecompileLiteral
import leo14.typed.decompile
import leo14.typed.process

val types = false

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

fun <T> Processor<Syntax>.process(parser: FunctionParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.function)) of typeKeywordKind)
		.process(parser.function.takes, parser.parentCompiledParser.context.dictionary)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.does)) of typeKeywordKind)
		.process(parser.function.does.type, parser.parentCompiledParser.context.dictionary)
		.process(token(end) of typeKeywordKind)

fun <T> Processor<Syntax>.process(parser: ArrowParser<T>): Processor<Syntax> =
	this
		.ifNotNull(parser.parent.typeParser) { process(it) }
		.process(token(begin(parser.parent.typeParser.dictionary.function)) of typeKeywordKind)
		.process(parser.arrow, parser.parent.typeParser.dictionary)

fun <T> Processor<Syntax>.process(parser: ChoiceParser<T>): Processor<Syntax> =
	this
		.ifNotNull(parser.parent?.typeParser) { process(it) }
		.process(token(begin(parser.dictionary.choice)) of typeKeywordKind)
		.fold(parser.choice.optionStack.reverse) { process(it, parser.dictionary) }

fun <T> Processor<Syntax>.processWithTypes(compiledParser: CompiledParser<T>): Processor<Syntax> =
	if (types)
		this
			.process(compiledParser.compiled.typed.type, compiledParser.context.dictionary)
			.process(token(begin("with")) of valueKeywordKind)
			.process(compiledParser)
	else process(compiledParser)

fun <T> Processor<Syntax>.process(compiledParser: CompiledParser<T>): Processor<Syntax> =
	this
		.ifNotNull(compiledParser.parent) { process(it) }
		.run {
			when (compiledParser.phase) {
				Phase.COMPILER -> process(compiledParser.compiled.typed.type, compiledParser.context.dictionary)
				Phase.EVALUATOR -> syntaxProcess(compiledParser.decompile)
			}
		}

fun <T> Processor<Syntax>.process(parser: TypeParser<T>): Processor<Syntax> =
	this
		.ifNotNull(parser.parent) { process(it) }
		.ifNotNull(parser.beginner) { process(it) }
		.process(parser.type, parser.dictionary)

fun <T> Processor<Syntax>.process(parser: DeleteParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.delete)) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parser: NothingParser<T>): Processor<Syntax> =
	process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.nothing)) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parser: ScriptParser<T>): Processor<Syntax> =
	this
		.process(parser.parent)
		.syntaxProcess(parser.script)

fun <T> Processor<Syntax>.process(parser: LeonardoParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.leonardo)) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parser: ForgetEverythingParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.forget)) of valueKeywordKind)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.everything)) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parser: ForgetEverythingEndParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.forget)) of valueKeywordKind)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.everything)) of valueKeywordKind)
		.process(token(end) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parent: ScriptParserParent<T>): Processor<Syntax> =
	when (parent) {
		is FieldScriptParserParent -> this
			.process(parent.scriptParser)
			.process(token(begin(parent.name)) of valueKind)
		is MakeScriptParserParent -> this
			.process(parent.compiledParser)
			.process(token(begin(parent.compiledParser.context.dictionary.make)) of valueKind)
		is CommentScriptParserParent -> this
			.process(parent.compiler)
			.process(token(begin(defaultDictionary.comment)) of commentKind)
		is CompiledScriptParserParent -> this
			.process(parent.compiledParser)
			.process(token(begin(defaultDictionary.script)) of commentKind)
	}

fun <T> Processor<Syntax>.process(matchParser: MatchParser<T>): Processor<Syntax> =
	this
		.process(matchParser.parentCompiledParser)
		.process(token(begin(matchParser.parentCompiledParser.context.dictionary.match)) of valueKeywordKind)
		.fold(matchParser.caseLineStack.reverse) { process(it, matchParser.parentCompiledParser.context.dictionary) }

fun <T> Processor<Syntax>.process(parent: CompiledParserParent<T>): Processor<Syntax> =
	when (parent) {
		is FunctionDoesParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.function)) of valueKeywordKind)
				.process(parent.type, parent.compiledParser.context.dictionary)
				.process(token(begin(parent.compiledParser.context.dictionary.does)) of valueKeywordKind)
		is FunctionGiveParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.`do`)) of valueKeywordKind)
		is FieldCompiledParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.name)) of valueKind)
		is RememberIsParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.remember)) of valueKeywordKind)
				.process(parent.type, parent.compiledParser.context.dictionary)
				.process(token(begin(parent.compiledParser.context.dictionary.`is`)) of valueKeywordKind)
		is RememberDoesParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.remember)) of valueKeywordKind)
				.process(parent.type, parent.compiledParser.context.dictionary)
				.process(token(begin(parent.compiledParser.context.dictionary.does)) of valueKeywordKind)
		is GiveCompiledParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.give)) of valueKeywordKind)
		is UseCompiledParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.use)) of valueKeywordKind)
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
				.process(token(begin(parent.typeParser.dictionary.function)) of typeKeywordKind)
				.process(parent.typeParser)
				.process(token(begin(parent.typeParser.dictionary.giving)) of typeKeywordKind)
		is AsTypeParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.`as`)) of valueKeywordKind)
		is OptionTypeParserParent ->
			this
				.process(parent.choiceParser)
				.process(token(begin(parent.name)) of typeKind)
		is ForgetTypeParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.forget)) of valueKeywordKind)
	}

fun <T> Processor<Syntax>.process(beginner: TypeBeginner<T>): Processor<Syntax> =
	when (beginner) {
		is FunctionGivesTypeBeginner ->
			this
				.process(beginner.compiledParser)
				.process(token(begin(beginner.compiledParser.context.dictionary.function)) of valueKeywordKind)
		is ArrowGivingTypeBeginner ->
			this
				.process(beginner.typeParser)
				.process(token(begin(beginner.typeParser.dictionary.giving)) of valueKeywordKind)
		is RememberTypeBeginner ->
			this
				.process(beginner.compiledParser)
				.process(token(begin(beginner.compiledParser.context.dictionary.remember)) of valueKeywordKind)
		is ForgetTypeBeginner -> this
	}

fun <T> Processor<Syntax>.process(parser: MemoryItemParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.remember)) of valueKeywordKind)
		.process(parser.memoryItem, parser.parentCompiledParser.context.dictionary, parser.parentCompiledParser.context.decompileLiteral)

fun <T> Processor<Syntax>.process(
	memoryItem: MemoryItem<T>,
	dictionary: Dictionary,
	decompileLiteral: DecompileLiteral<T>): Processor<Syntax> =
	when (memoryItem) {
		is RememberMemoryItem ->
			process(memoryItem.function.takes, dictionary)
				.process(token(begin(
					if (memoryItem.needsInvoke) dictionary.does
					else dictionary.`is`)) of valueKeywordKind)
				.run {
					if (memoryItem.needsInvoke) process(memoryItem.function.does.type, dictionary)
					else syntaxProcess(memoryItem.function.does.decompile(decompileLiteral))
				}
				.process(token(end) of valueKeywordKind)
		else -> error("$this.process($memoryItem)")
	}
