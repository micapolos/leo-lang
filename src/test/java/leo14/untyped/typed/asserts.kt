package leo14.untyped.typed

import leo.base.assertEqualTo

val <T> Compiled<T>.assertEvaluatedOnce: Compiled<T>
	get() = type.compiled(expression.assertEvaluatesOnce)

val <T> Dynamic<T>.assertEvaluatesOnce: Dynamic<T>
	get() {
		var evaluated = false
		return dynamic {
			evaluated.assertEqualTo(false, "Evaluated twice")
			evaluated = true
			value
		}
	}

val <T> Expression<T>.assertEvaluatesOnce: Expression<T>
	get() =
		when (this) {
			is ConstantExpression -> this
			is DynamicExpression -> dynamic.assertEvaluatesOnce.expression
		}