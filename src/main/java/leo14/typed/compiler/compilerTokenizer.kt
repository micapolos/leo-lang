package leo14.typed.compiler

import leo.base.ifNotNull
import leo.base.runIf
import leo13.fold
import leo13.reverse
import leo14.*
import leo14.reader.CompilerTokenReader
import leo14.reader.FragmentTokenReader
import leo14.reader.TokenReader
import leo14.syntax.*
import leo14.typed.isStatic
import leo14.typed.process

val types = true

fun Processor<Syntax>.process(tokenReader: TokenReader): Processor<Syntax> =
	when (tokenReader) {
		is CompilerTokenReader<*> -> process(tokenReader.compiler)
		is FragmentTokenReader -> process(tokenReader.fragment)
	}

fun <T> Processor<Syntax>.process(compiler: Compiler<T>): Processor<Syntax> =
	when (compiler) {
		is ActionParserCompiler -> process(compiler.functionParser)
		is ArrowParserCompiler -> process(compiler.arrowParser)
		is ChoiceParserCompiler -> process(compiler.choiceParser)
		is CompiledParserCompiler -> process(compiler.compiledParser)
		is DefineParserCompiler -> process(compiler.defineParser)
		is TypeParserCompiler -> process(compiler.typeParser)
		is MatchParserCompiler -> process(compiler.matchParser)
		is QuoteParserCompiler -> process(compiler.quoteParser)
	}

fun Processor<Syntax>.process(fragment: Fragment): Processor<Syntax> =
	this
		.ifNotNull(fragment.parent) { process(it) }
		.syntaxProcess(fragment.script)

fun Processor<Syntax>.process(fragmentParent: FragmentParent): Processor<Syntax> =
	this
		.process(fragmentParent.fragment)
		.process(token(fragmentParent.begin) of valueKeywordKind)

fun <T> Processor<Syntax>.process(parser: FunctionParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(Keyword.FUNCTION stringIn parser.parentCompiledParser.context.language)) of typeKeywordKind)
		.process(parser.function.takes, parser.parentCompiledParser.context.language)
		.process(token(begin(Keyword.GIVES stringIn parser.parentCompiledParser.context.language)) of typeKeywordKind)
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

fun <T> Processor<Syntax>.process(compiledParser: CompiledParser<T>): Processor<Syntax> =
	processWithoutTypes(compiledParser)
		.runIf(types && compiledParser.kind == CompilerKind.EVALUATOR && !compiledParser.compiled.typed.type.isStatic) {
			this
				.process(token(begin("of")) of valueKeywordKind)
				.process(compiledParser.compiled.typed.type, compiledParser.context.language)
				.process(token(end) of valueKeywordKind)
		}

fun <T> Processor<Syntax>.processWithoutTypes(compiledParser: CompiledParser<T>): Processor<Syntax> =
	this
		.ifNotNull(compiledParser.parent) { process(it) }
		.run {
			when (compiledParser.kind) {
				CompilerKind.COMPILER -> process(compiledParser.compiled.typed.type, compiledParser.context.language)
				CompilerKind.EVALUATOR -> syntaxProcess(compiledParser.decompile)
			}
		}

fun <T> Processor<Syntax>.process(parser: TypeParser<T>): Processor<Syntax> =
	this
		.ifNotNull(parser.parent) { process(it) }
		.ifNotNull(parser.beginner) { process(it) }
		.process(parser.type, parser.language)

fun <T> Processor<Syntax>.process(parser: QuoteParser<T>): Processor<Syntax> =
	this
		.process(parser.parent)
		.syntaxProcess(parser.script)

fun <T> Processor<Syntax>.process(parent: QuoteParserParent<T>): Processor<Syntax> =
	when (parent) {
		is FieldQuoteParserParent -> this
			.process(parent.quoteParser)
			.process(token(begin(parent.name)) of valueKind)
		is MakeQuoteParserParent -> this
			.process(parent.compiledParser)
			.process(token(begin(Keyword.MAKE stringIn parent.compiledParser.context.language)) of valueKind)
		is CommentQuoteParserParent -> this
			.process(parent.compiler)
			.process(token(begin(Keyword.COMMENT stringIn defaultLanguage)) of commentKind)
		is CompiledQuoteParserParent -> this
			.process(parent.compiledParser)
			.process(token(begin(Keyword.QUOTE stringIn defaultLanguage)) of commentKind)
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
				.process(token(begin(Keyword.GIVES stringIn parent.compiledParser.context.language)) of valueKeywordKind)
		is FieldCompiledParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.name)) of valueKind)
		is DefineIsParserParent ->
			this
				.process(parent.defineParser)
				.process(parent.type, parent.defineParser.parentCompiledParser.context.language)
				.process(token(begin(Keyword.IS stringIn parent.defineParser.parentCompiledParser.context.language)) of valueKeywordKind)
		is DefineGivesParserParent ->
			this
				.process(parent.defineParser)
				.process(parent.type, parent.defineParser.parentCompiledParser.context.language)
				.process(token(begin(Keyword.GIVES stringIn parent.defineParser.parentCompiledParser.context.language)) of valueKeywordKind)
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
		is CompiledTypeParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.name)) of typeKeywordKind)
		is ArrowGivesTypeParserParent ->
			this
				.process(token(begin(Keyword.FUNCTION stringIn parent.typeParser.language)) of typeKeywordKind)
				.process(parent.typeParser)
				.process(token(begin(Keyword.GIVES stringIn parent.typeParser.language)) of typeKeywordKind)
		is AsTypeParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(Keyword.AS stringIn parent.compiledParser.context.language)) of valueKeywordKind)
		is OptionTypeParserParent ->
			this
				.process(parent.choiceParser)
				.process(token(begin(parent.name)) of typeKind)
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
		is DefineTypeBeginner ->
			process(beginner.defineParser)
	}

fun <T> Processor<Syntax>.process(parser: DefineParser<T>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(Keyword.DEFINE stringIn parser.parentCompiledParser.context.language)) of valueKeywordKind)

fun <T> Processor<Syntax>.process(
	memoryItem: MemoryItem<T>,
	language: Language): Processor<Syntax> =
	syntaxProcess(script(memoryItem.reflectScriptLine { line(literal(toString())) }))
