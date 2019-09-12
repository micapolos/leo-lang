package leo13.untyped.evaluator

data class EvaluatorLink(val evaluator: Evaluator, val name: String)
infix fun Evaluator.linkTo(name: String) = EvaluatorLink(this, name)


