package leo16

import leo14.run

val importBase = true

fun main() {
	val evaluator = if (importBase) baseEvaluator else emptyEvaluator
	run(evaluator.stringCharReducer)
}
