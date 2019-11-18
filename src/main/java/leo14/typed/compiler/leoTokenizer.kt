package leo14.typed.compiler

import leo.base.ifNotNull
import leo14.*
import leo14.typed.decompile

fun <T> Processor<Token>.process(leo: Leo<T>): Processor<Token> =
	when (leo) {
		is CompiledParserLeo -> process(leo.compiledParser)
		else -> TODO()
	}

fun <T> Processor<Token>.process(compiledParser: CompiledParser<T>): Processor<Token> =
	this
		.ifNotNull(compiledParser.parent) { process(it) }
		.process(compiledParser.compiled.typed.decompile { error("") })

fun <T> Processor<Token>.process(compiledParserParent: CompiledParserParent<T>): Processor<Token> =
	when (compiledParserParent) {
		is FieldCompiledParserParent ->
			this
				.process(compiledParserParent.compiledParser)
				.process(token(begin(compiledParserParent.name)))
		else -> TODO()
	}
