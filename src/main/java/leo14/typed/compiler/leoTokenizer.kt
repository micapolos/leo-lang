package leo14.typed.compiler

import leo.base.ifNotNull
import leo14.*
import leo14.native.Native
import leo14.typed.nativeDecompile
import leo14.typed.process

fun Processor<Token>.process(leo: Leo<Native>): Processor<Token> =
	when (leo) {
		is ChoiceParserLeo -> process(leo.choiceParser)
		is ArrowParserLeo -> process(leo.arrowParser)
		is CompiledParserLeo -> process(leo.compiledParser)
		is TypeParserLeo -> process(leo.typeParser)
		is RememberParserLeo -> process(leo.memoryItemParser)
		is DeleteParserLeo -> process(leo.deleteParser)
		else -> TODO()
	}

fun Processor<Token>.process(parser: ArrowParser<Native>): Processor<Token> =
	this
		.ifNotNull(parser.parent.typeParser) { process(it) }
		.process(token(begin(parser.parent.typeParser.dictionary.action)))
		.process(parser.arrow, parser.parent.typeParser.dictionary)

fun Processor<Token>.process(parser: ChoiceParser<Native>): Processor<Token> =
	this
		.ifNotNull(parser.parent?.typeParser) { process(it) }
		.process(token(begin(parser.dictionary.choice)))
		.process(parser.choice, parser.dictionary)

fun Processor<Token>.process(compiledParser: CompiledParser<Native>): Processor<Token> =
	this
		.ifNotNull(compiledParser.parent) { process(it) }
		.process(compiledParser.compiled.typed.nativeDecompile)

fun Processor<Token>.process(parser: TypeParser<Native>): Processor<Token> =
	this
		.ifNotNull(parser.parent) { process(it) }
		.ifNotNull(parser.beginner) { process(it) }
		.process(parser.type, parser.dictionary)

fun Processor<Token>.process(parser: DeleteParser<Native>): Processor<Token> =
	process(parser.parentCompiledParser)
		.process(token(begin(parser.parentCompiledParser.context.dictionary.delete)))

fun Processor<Token>.process(parent: CompiledParserParent<Native>): Processor<Token> =
	when (parent) {
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
		else -> TODO()
	}

fun Processor<Token>.process(parent: TypeParserParent<Native>): Processor<Token> =
	when (parent) {
		is LineTypeParserParent -> process(parent.typeParser).process(token(begin(parent.name)))
		else -> TODO()
	}

fun Processor<Token>.process(beginner: TypeBeginner<Native>): Processor<Token> =
	when (beginner) {
		is RememberTypeBeginner ->
			this
				.process(beginner.compiledParser)
				.process(token(begin(beginner.compiledParser.context.dictionary.remember)))
		else -> TODO()
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
				.process(memoryItem.action.body.nativeDecompile)
				.process(token(end))
		else -> TODO()
	}
