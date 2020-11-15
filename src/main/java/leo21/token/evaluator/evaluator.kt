package leo21.token.evaluator

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.evaluator.Evaluated
import leo21.token.processor.TokenProcessor

sealed class EvaluatorParent

data class Evaluator(
	val parentOrNull: EvaluatorParent?,
	val context: Context,
	val evaluated: Evaluated
)

fun Evaluator.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> TODO()
		is BeginToken -> TODO()
		is EndToken -> parentOrNull!!.end(evaluated)
	}

fun EvaluatorParent.end(evaluated: Evaluated): TokenProcessor =
	TODO()

fun Evaluator.set(evaluated: Evaluated): Evaluator =
	copy(evaluated = evaluated)