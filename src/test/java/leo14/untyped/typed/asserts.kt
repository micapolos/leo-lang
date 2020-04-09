package leo14.untyped.typed

import leo.base.assertEqualTo

val Compiled.assertEvaluatedOnce: Compiled
	get() = type.compiled(expression.assertEvaluatesOnce)

val Dynamic.assertEvaluatesOnce: Dynamic
	get() {
		var evaluated = false
		return dynamic {
			evaluated.assertEqualTo(false, "Evaluated twice")
			evaluated = true
			value
		}
	}

val Expression.assertEvaluatesOnce: Expression
	get() =
		when (this) {
			is ConstantExpression -> this
			is DynamicExpression -> dynamic.assertEvaluatesOnce.expression
		}