package leo13.untyped.evaluator

import leo13.untyped.compiler.Compiled

data class EvaluatorLink(val evaluator: Evaluator, val name: String)
infix fun Evaluator.linkTo(name: String) = EvaluatorLink(this, name)

fun EvaluatorLink.evaluator(compiled: Compiled): Evaluator? =
	TODO()


