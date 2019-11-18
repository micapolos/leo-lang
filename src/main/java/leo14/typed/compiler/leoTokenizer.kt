package leo14.typed.compiler

import leo.base.ifNotNull
import leo14.*
import leo14.native.Native
import leo14.typed.nativeDecompile

fun Processor<Token>.process(leo: Leo<Native>): Processor<Token> =
	when (leo) {
		is CompiledParserLeo -> process(leo.compiledParser)
		else -> TODO()
	}

fun Processor<Token>.process(compiledParser: CompiledParser<Native>): Processor<Token> =
	this
		.ifNotNull(compiledParser.parent) { process(it) }
		.process(compiledParser.compiled.typed.nativeDecompile)

fun Processor<Token>.process(compiledParserParent: CompiledParserParent<Native>): Processor<Token> =
	when (compiledParserParent) {
		is FieldCompiledParserParent ->
			this
				.process(compiledParserParent.compiledParser)
				.process(token(begin(compiledParserParent.name)))
		else -> TODO()
	}
