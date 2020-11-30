package leo23.evaluator

import leo13.Stack
import leo13.map
import leo14.Script
import leo14.script
import leo23.typed.value.Evaluated
import leo23.typed.value.scriptLine

data class Evaluator(
	val evaluatedStack: Stack<Evaluated>
)

val Evaluator.printScript: Script
	get() =
		evaluatedStack.map { scriptLine }.script