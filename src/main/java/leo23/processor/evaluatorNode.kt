package leo23.processor

import leo14.Begin
import leo14.BeginToken
import leo14.EndToken
import leo14.Fragment
import leo14.FragmentParent
import leo14.LiteralToken
import leo14.Token
import leo14.fragment
import leo14.parent
import leo23.evaluator.Evaluator
import leo23.evaluator.printScript

data class EvaluatorNode(
	val parentOrNull: EvaluatorParent?,
	val evaluator: Evaluator
)

sealed class EvaluatorParent
data class BeginEvaluatorParent(val node: EvaluatorNode, val begin: Begin) : EvaluatorParent()

fun EvaluatorNode.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> TODO()
		is BeginToken -> TODO()
		is EndToken -> TODO()
	}

val EvaluatorNode.printFragment: Fragment
	get() =
		parentOrNull?.printFragmentParent.fragment(evaluator.printScript)

val EvaluatorParent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is BeginEvaluatorParent -> node.printFragment.parent(begin)
		}