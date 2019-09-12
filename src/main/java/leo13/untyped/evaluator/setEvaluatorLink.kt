package leo13.untyped.evaluator

import leo13.untyped.value.Value
import leo13.untyped.value.lineTo

data class SetEvaluatorLink(val setEvaluator: SetEvaluator, val name: String)

infix fun SetEvaluator.linkTo(name: String) =
	SetEvaluatorLink(this, name)

fun SetEvaluatorLink.setEvaluator(value: Value) =
	setEvaluator.plus(name lineTo value)