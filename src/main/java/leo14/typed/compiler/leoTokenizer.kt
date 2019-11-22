package leo14.typed.compiler

import leo.base.ifNotNull
import leo13.fold
import leo13.reverse
import leo14.*
import leo14.native.Native
import leo14.syntax.*
import leo14.typed.decompile
import leo14.typed.process

val types = false

fun Processor<Syntax>.process(compiler: Compiler<Native>): Processor<Syntax> =
	when (compiler) {
		is ActionParserCompiler -> process(compiler.actionParser)
		is ArrowParserCompiler -> process(compiler.arrowParser)
		is ChoiceParserCompiler -> process(compiler.choiceParser)
		is CompiledParserCompiler -> processWithTypes(compiler.compiledParser)
		is DeleteParserCompiler -> process(compiler.deleteParser)
		is NativeParserCompiler -> process(compiler.nativeParser)
		is NothingParserCompiler -> process(compiler.nothingParser)
		is RememberParserCompiler -> process(compiler.memoryItemParser)
		is TypeParserCompiler -> process(compiler.typeParser)
		is MatchParserCompiler -> process(compiler.matchParser)
		is ScriptParserCompiler -> process(compiler.scriptParser)
	}

fun Processor<Syntax>.process(parser: ActionParser<Native>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.action)) of typeKeywordKind)
		.process(parser.action.param, parser.parentCompiledParser.context.dictionary)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.does)) of typeKeywordKind)
		.process(parser.action.body.type, parser.parentCompiledParser.context.dictionary)
		.process(token(end) of typeKeywordKind)

fun Processor<Syntax>.process(parser: ArrowParser<Native>): Processor<Syntax> =
	this
		.ifNotNull(parser.parent.typeParser) { process(it) }
		.process(token(begin(parser.parent.typeParser.dictionary.action)) of typeKeywordKind)
		.process(parser.arrow, parser.parent.typeParser.dictionary)

fun Processor<Syntax>.process(parser: ChoiceParser<Native>): Processor<Syntax> =
	this
		.ifNotNull(parser.parent?.typeParser) { process(it) }
		.process(token(begin(parser.dictionary.choice)) of typeKeywordKind)
		.fold(parser.choice.optionStack.reverse) { process(it, parser.dictionary) }

fun Processor<Syntax>.processWithTypes(compiledParser: CompiledParser<Native>): Processor<Syntax> =
	if (types)
		this
			.process(compiledParser.compiled.typed.type, compiledParser.context.dictionary)
			.process(token(begin("with")) of valueKeywordKind)
			.process(compiledParser)
	else process(compiledParser)

fun Processor<Syntax>.process(compiledParser: CompiledParser<Native>): Processor<Syntax> =
	this
		.ifNotNull(compiledParser.parent) { process(it) }
		.run {
			when (compiledParser.phase) {
				Phase.COMPILER -> process(compiledParser.compiled.typed.type, compiledParser.context.dictionary)
				Phase.EVALUATOR -> syntaxProcess(compiledParser.compiled.typed.decompile)
			}
		}

fun Processor<Syntax>.process(parser: TypeParser<Native>): Processor<Syntax> =
	this
		.ifNotNull(parser.parent) { process(it) }
		.ifNotNull(parser.beginner) { process(it) }
		.process(parser.type, parser.dictionary)

fun Processor<Syntax>.process(parser: DeleteParser<Native>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.delete)) of valueKeywordKind)

fun Processor<Syntax>.process(parser: NativeParser<Native>): Processor<Syntax> =
	this
		.process(parser.parentTypeParser)
		.process(token(begin(parser.parentTypeParser.dictionary.native)) of valueKeywordKind)

fun Processor<Syntax>.process(parser: NothingParser<Native>): Processor<Syntax> =
	process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.nothing)) of valueKeywordKind)

fun Processor<Syntax>.process(parser: ScriptParser<Native>): Processor<Syntax> =
	this
		.process(parser.parent)
		.syntaxProcess(parser.script)

fun Processor<Syntax>.process(parent: ScriptParserParent<Native>): Processor<Syntax> =
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
	}

fun Processor<Syntax>.process(matchParser: MatchParser<Native>): Processor<Syntax> =
	this
		.process(matchParser.parentCompiledParser)
		.process(token(begin(matchParser.parentCompiledParser.context.dictionary.match)) of valueKeywordKind)
		.fold(matchParser.caseLineStack.reverse) { process(it, matchParser.parentCompiledParser.context.dictionary) }

fun Processor<Syntax>.process(parent: CompiledParserParent<Native>): Processor<Syntax> =
	when (parent) {
		is ActionDoesParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.action)) of valueKeywordKind)
				.process(parent.type, parent.compiledParser.context.dictionary)
				.process(token(begin(parent.compiledParser.context.dictionary.does)) of valueKeywordKind)
		is ActionDoParserParent ->
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
		is MatchParserParent ->
			this
				.process(parent.matchParser)
				.process(token(begin(parent.name)) of valueKeywordKind)
	}

fun Processor<Syntax>.process(parent: TypeParserParent<Native>): Processor<Syntax> =
	when (parent) {
		is LineTypeParserParent ->
			this
				.process(parent.typeParser)
				.process(token(begin(parent.name)) of typeKeywordKind)
		is ArrowGivingTypeParserParent ->
			this
				.process(token(begin(parent.typeParser.dictionary.action)) of typeKeywordKind)
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

fun Processor<Syntax>.process(beginner: TypeBeginner<Native>): Processor<Syntax> =
	when (beginner) {
		is ActionDoesTypeBeginner ->
			this
				.process(beginner.compiledParser)
				.process(token(begin(beginner.compiledParser.context.dictionary.action)) of valueKeywordKind)
		is ArrowGivingTypeBeginner ->
			this
				.process(beginner.typeParser)
				.process(token(begin(beginner.typeParser.dictionary.giving)) of valueKeywordKind)
		is RememberTypeBeginner ->
			this
				.process(beginner.compiledParser)
				.process(token(begin(beginner.compiledParser.context.dictionary.remember)) of valueKeywordKind)
	}

fun Processor<Syntax>.process(parser: MemoryItemParser<Native>): Processor<Syntax> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.remember)) of valueKeywordKind)
		.process(parser.memoryItem, parser.parentCompiledParser.context.dictionary)

fun Processor<Syntax>.process(memoryItem: MemoryItem<Native>, dictionary: Dictionary): Processor<Syntax> =
	when (memoryItem) {
		is RememberMemoryItem ->
			process(memoryItem.action.param, dictionary)
				.process(token(begin(
					if (memoryItem.needsInvoke) dictionary.does
					else dictionary.`is`)) of valueKeywordKind)
				.run {
					if (memoryItem.needsInvoke) process(memoryItem.action.body.type, dictionary)
					else syntaxProcess(memoryItem.action.body.decompile)
				}
				.process(token(end) of valueKeywordKind)
		else -> error("$this.process($memoryItem)")
	}
