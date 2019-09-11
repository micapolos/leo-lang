package leo13.untyped.evaluator

import leo13.untyped.TokenReader
import leo13.untyped.compiler.CompiledLine

data class Evaluator(
	val context: Context,
	val evaluated: Evaluated) : TokenReader {
	override fun begin(name: String) = TODO()
	override val end = TODO()
}

fun Evaluator.plus(line: CompiledLine): Evaluator? =
	when (line.name) {
		else -> TODO()
	}
