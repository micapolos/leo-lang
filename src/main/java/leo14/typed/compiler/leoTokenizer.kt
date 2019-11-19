package leo14.typed.compiler

import leo.base.ifNotNull
import leo.base.runIf
import leo13.fold
import leo13.reverse
import leo14.*
import leo14.native.Native
import leo14.typed.nativeDecompile
import leo14.typed.process

val types = false

fun Processor<Token>.process(leo: Leo<Native>): Processor<Token> =
	when (leo) {
		is ActionParserLeo -> process(leo.actionParser)
		is ArrowParserLeo -> process(leo.arrowParser)
		is ChoiceParserLeo -> process(leo.choiceParser)
		is CommentParserLeo -> process(leo.commentParser)
		is CompiledParserLeo -> process(leo.compiledParser)
		is DeleteParserLeo -> process(leo.deleteParser)
		is NativeParserLeo -> process(leo.nativeParser)
		is NothingParserLeo -> process(leo.nothingParser)
		is RememberParserLeo -> process(leo.memoryItemParser)
		is TypeParserLeo -> process(leo.typeParser)
	}

fun Processor<Token>.process(parser: ActionParser<Native>): Processor<Token> =
	this
		.process(parser.parentCompiledParser)
		.process(parser.action, parser.parentCompiledParser.context.dictionary)

fun Processor<Token>.process(parser: ArrowParser<Native>): Processor<Token> =
	this
		.ifNotNull(parser.parent.typeParser) { process(it) }
		.process(token(begin(parser.parent.typeParser.dictionary.action)))
		.process(parser.arrow, parser.parent.typeParser.dictionary)

fun Processor<Token>.process(parser: ChoiceParser<Native>): Processor<Token> =
	this
		.ifNotNull(parser.parent?.typeParser) { process(it) }
		.process(token(begin(parser.dictionary.choice)))
		.fold(parser.choice.optionStack.reverse) { process(it, parser.dictionary) }

fun Processor<Token>.process(compiledParser: CompiledParser<Native>): Processor<Token> =
	this
		.ifNotNull(compiledParser.parent) { process(it) }
		.run {
			when (compiledParser.phase) {
				Phase.COMPILER -> process(compiledParser.compiled.typed.type, compiledParser.context.dictionary)
				Phase.EVALUATOR -> process(compiledParser.compiled.typed.nativeDecompile)
			}
		}
		.runIf(types) {
			this
				.process(token(begin(compiledParser.context.dictionary.`as`)))
				.process(compiledParser.compiled.typed.type, compiledParser.context.dictionary)
				.process(token(end))
		}

fun Processor<Token>.process(parser: TypeParser<Native>): Processor<Token> =
	this
		.ifNotNull(parser.parent) { process(it) }
		.ifNotNull(parser.beginner) { process(it) }
		.process(parser.type, parser.dictionary)

fun Processor<Token>.process(parser: DeleteParser<Native>): Processor<Token> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.delete)))

fun Processor<Token>.process(parser: NativeParser<Native>): Processor<Token> =
	this
		.process(parser.parentTypeParser)
		.process(token(begin(parser.parentTypeParser.dictionary.native)))

fun Processor<Token>.process(parser: NothingParser<Native>): Processor<Token> =
	process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.nothing)))

fun Processor<Token>.process(parser: CommentParser<Native>): Processor<Token> =
	this
		.process(parser.parent)
		.process(token(begin(defaultDictionary.comment))) // TODO: Leo needs dictionary!!!

fun Processor<Token>.process(parent: CompiledParserParent<Native>): Processor<Token> =
	when (parent) {
		is ActionDoesParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.action)))
				.process(parent.type, parent.compiledParser.context.dictionary)
				.process(token(begin(parent.compiledParser.context.dictionary.does)))
		is ActionDoParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.`do`)))
		is FieldCompiledParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.name)))
		is RememberIsParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.remember)))
				.process(parent.type, parent.compiledParser.context.dictionary)
				.process(token(begin(parent.compiledParser.context.dictionary.`is`)))
		is RememberDoesParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.remember)))
				.process(parent.type, parent.compiledParser.context.dictionary)
				.process(token(begin(parent.compiledParser.context.dictionary.does)))
		is GiveCompiledParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.give)))
	}

fun Processor<Token>.process(parent: TypeParserParent<Native>): Processor<Token> =
	when (parent) {
		is LineTypeParserParent ->
			this
				.process(parent.typeParser)
				.process(token(begin(parent.name)))
		is ArrowGivingTypeParserParent ->
			this
				.process(token(begin(parent.typeParser.dictionary.action)))
				.process(parent.typeParser)
				.process(token(begin(parent.typeParser.dictionary.giving)))
		is AsTypeParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.`as`)))
		is OptionTypeParserParent ->
			this
				.process(parent.choiceParser)
				.process(token(begin(parent.name)))
		is ForgetTypeParserParent ->
			this
				.process(parent.compiledParser)
				.process(token(begin(parent.compiledParser.context.dictionary.forget)))
	}

fun Processor<Token>.process(beginner: TypeBeginner<Native>): Processor<Token> =
	when (beginner) {
		is ActionDoesTypeBeginner ->
			this
				.process(beginner.compiledParser)
				.process(token(begin(beginner.compiledParser.context.dictionary.action)))
		is ArrowGivingTypeBeginner ->
			this
				.process(beginner.typeParser)
				.process(token(begin(beginner.typeParser.dictionary.giving)))
		is RememberTypeBeginner ->
			this
				.process(beginner.compiledParser)
				.process(token(begin(beginner.compiledParser.context.dictionary.remember)))
	}

fun Processor<Token>.process(parser: MemoryItemParser<Native>): Processor<Token> =
	this
		.process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.remember)))
		.process(parser.memoryItem, parser.parentCompiledParser.context.dictionary)

fun Processor<Token>.process(memoryItem: MemoryItem<Native>, dictionary: Dictionary): Processor<Token> =
	when (memoryItem) {
		is RememberMemoryItem ->
			process(memoryItem.action.param, dictionary)
				.process(token(begin(
					if (memoryItem.needsInvoke) dictionary.does
					else dictionary.`is`)))
				.run {
					if (memoryItem.needsInvoke) process(memoryItem.action.body.type, dictionary)
					else process(memoryItem.action.body.nativeDecompile)
				}
				.process(token(end))
		else -> error("$this.process($memoryItem)")
	}
