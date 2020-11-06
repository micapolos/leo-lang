package leo21.token.evaluator

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.evaluator.Evaluated
import leo21.evaluator.Evaluator
import leo21.evaluator.emptyEvaluated
import leo21.evaluator.emptyEvaluator
import leo21.evaluator.plus
import leo21.token.processor.EvaluatorTokenProcessor
import leo21.token.processor.TokenProcessor

data class TokenEvaluator(
	val parentOrNull: EvaluatedParent?,
	val evaluator: Evaluator
)

val emptyTokenEvaluator = TokenEvaluator(null, emptyEvaluator)

fun TokenEvaluator.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> EvaluatorTokenProcessor(copy(evaluator = evaluator.plus(token.literal)))
		is BeginToken -> when (token.begin.string) {
			"define" -> TODO()
			else ->
				EvaluatorTokenProcessor(
					TokenEvaluator(
						EvaluatorNameEvaluatedParent(this, token.begin.string),
						evaluator.copy(evaluated = emptyEvaluated)))
		}
		is EndToken -> parentOrNull!!.plus(evaluator.evaluated)
	}

fun TokenEvaluator.plus(name: String, evaluated: Evaluated): TokenEvaluator =
	copy(evaluator = evaluator.plus(name, evaluated))