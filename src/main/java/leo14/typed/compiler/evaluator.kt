package leo14.typed.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.typed.Typed

data class Evaluator<T>(
	val parent: EvaluatorParent<T>?,
	val compiledParser: CompiledParser<T>)

sealed class EvaluatorParent<T>

fun <T> Evaluator<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> leo(copy(compiledParser = compiledParser.plus(token.literal)))
		is BeginToken -> TODO()
		is EndToken -> parent?.ret(compiledParser.compiled.resolveForEnd.typed)
	} ?: error("$this.parse($token)")

fun <T> EvaluatorParent<T>.ret(typed: Typed<T>): Leo<T> =
	TODO()