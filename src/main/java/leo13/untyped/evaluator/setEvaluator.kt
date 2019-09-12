package leo13.untyped.evaluator

import leo13.untyped.TokenReader
import leo9.Stack
import leo9.push
import leo9.stack

data class SetEvaluator(val parentEvaluator: Evaluator, val lineStack: Stack<ValueLine>): TokenReader {
	override fun begin(name: String) =
		linkTo(name).parent.evaluator(parentEvaluator.context)

	override val end get() =
		parentEvaluator.plusSet(lineStack)
}

val Evaluator.setEvaluator get() = SetEvaluator(this, stack())

fun SetEvaluator.plus(line: ValueLine) =
	copy(lineStack = lineStack.push(line))
