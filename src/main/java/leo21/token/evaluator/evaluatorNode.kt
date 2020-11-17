package leo21.token.evaluator

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.evaluator.LineEvaluated
import leo21.evaluator.lineEvaluated
import leo21.token.processor.EvaluatorTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.token.processor.tokenProcessor

data class EvaluatorNode(
	val parentOrNull: EvaluatorParent?,
	val evaluator: Evaluator
)

val emptyEvaluatorNode = EvaluatorNode(null, emptyEvaluator)

fun EvaluatorNode.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> plus(token.literal.lineEvaluated).tokenProcessor
		is BeginToken -> when (token.begin.string) {
			"do" -> EvaluatorTokenProcessor(
				EvaluatorNode(
					EvaluatorNodeDoEvaluatorParent(EvaluatorNodeDo(this)),
					evaluator.beginDo))
			else -> EvaluatorTokenProcessor(
				EvaluatorNode(
					EvaluatorNodeBeginEvaluatorParent(EvaluatorNodeBegin(this, token.begin.string)),
					evaluator.begin))
		}

		is EndToken -> parentOrNull!!.end(evaluator)
	}

fun EvaluatorNode.plus(line: LineEvaluated): EvaluatorNode =
	copy(evaluator = evaluator.plus(line))

fun EvaluatorNode.end(field: EvaluatorField): EvaluatorNode =
	copy(evaluator = evaluator.plus(field))

fun EvaluatorNode.do_(evaluator: Evaluator): EvaluatorNode =
	copy(evaluator = evaluator.do_(evaluator))