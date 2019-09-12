package leo13.untyped.evaluator

import leo13.untyped.value.Value

sealed class EvaluatorParent

data class SelfEvaluatorParent(val link: EvaluatorLink): EvaluatorParent()
data class SetEvaluatorParent(val link: SetEvaluatorLink): EvaluatorParent()

val EvaluatorLink.parent get() = SelfEvaluatorParent(this)
val SetEvaluatorLink.parent get() = SetEvaluatorParent(this)

fun EvaluatorParent.evaluator(context: Context, value: Value = leo13.untyped.value.value()) =
	Evaluator(this, context, value)

fun EvaluatorParent.plus(value: Value) =
	when (this) {
		is SelfEvaluatorParent -> link.evaluator(value)
		is SetEvaluatorParent -> link.setEvaluator(value)
	}