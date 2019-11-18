package leo14.typed.compiler

import leo.base.ifNotNull
import leo14.*
import leo14.native.Native
import leo14.typed.nativeDecompile
import leo14.typed.process

fun Processor<Token>.process(leo: Leo<Native>): Processor<Token> =
	when (leo) {
		is CompiledParserLeo -> process(leo.compiledParser)
		is TypeParserLeo -> process(leo.typeParser)
		is RememberParserLeo -> process(leo.memoryItemParser)
		is DeleteParserLeo -> process(leo.deleteParser)
		else -> TODO()
	}

fun Processor<Token>.process(compiledParser: CompiledParser<Native>): Processor<Token> =
	this
		.ifNotNull(compiledParser.parent) { process(it) }
		.process(compiledParser.compiled.typed.nativeDecompile)

fun Processor<Token>.process(parser: TypeParser<Native>): Processor<Token> =
	this
		.ifNotNull(parser.parent) { process(it) }
		.ifNotNull(parser.beginner) { process(it) }
		.process(parser.type)

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
				.process(token(begin("remember")))
				.process(parent.type)
				.process(token(begin("is")))
		else -> TODO()
	}

fun Processor<Token>.process(parent: TypeParserParent<Native>): Processor<Token> =
	when (parent) {
		is LineTypeParserParent -> process(parent.typeParser).process(token(begin(parent.name)))
		else -> TODO()
	}

fun Processor<Token>.process(beginner: TypeBeginner<Native>): Processor<Token> =
	when (beginner) {
		is RememberTypeBeginner -> process(beginner.compiledParser).process(token(begin("remember")))
		else -> TODO()
	}

fun Processor<Token>.process(parser: MemoryItemParser<Native>): Processor<Token> =
	process(parser.parentCompiledParser).process(token(begin("remember"))).process(parser.memoryItem)

fun Processor<Token>.process(memoryItem: MemoryItem<Native>): Processor<Token> =
	when (memoryItem) {
		is RememberMemoryItem ->
			process(memoryItem.action.param)
				.process(token(begin(if (memoryItem.needsInvoke) "does" else "is")))
				.process(memoryItem.action.body.nativeDecompile)
				.process(token(end))
		else -> TODO()
	}
