@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

typealias V = Any?
typealias Fn = () -> V

data class Constant(val value: V)
data class Dynamic(val evaluate: Fn)

sealed class Expression
data class ConstantExpression(val constant: Constant) : Expression()
data class DynamicExpression(val dynamic: Dynamic) : Expression()

fun constant(value: V) = Constant(value)
fun dynamic(evaluate: Fn) = Dynamic(evaluate)
val Constant.expression: Expression get() = ConstantExpression(this)
val Dynamic.expression: Expression get() = DynamicExpression(this)
fun expression(value: V): Expression = constant(value).expression
fun expression(fn: Fn): Expression = dynamic(fn).expression

val Dynamic.value: V
	get() =
		evaluate()

val Expression.value: V
	get() =
		when (this) {
			is ConstantExpression -> constant.value
			is DynamicExpression -> dynamic.value
		}

val Expression.evaluate: Expression
	get() =
		expression(value)

inline fun Expression.doApply(crossinline fn: V.() -> V): Expression =
	when (this) {
		is ConstantExpression -> expression(constant.value.fn())
		is DynamicExpression -> dynamic.evaluate.let { evaluate ->
			expression { evaluate().fn() }
		}
	}

inline fun Expression.doApply(rhs: Expression, crossinline fn: V.(V) -> V): Expression =
	when (this) {
		is ConstantExpression -> {
			val lhsValue = constant.value
			when (rhs) {
				is ConstantExpression -> {
					val rhsValue = rhs.constant.value
					expression(lhsValue.fn(rhsValue))
				}
				is DynamicExpression -> {
					val rhsEvaluate = rhs.dynamic.evaluate
					expression { lhsValue.fn(rhsEvaluate()) }
				}
			}
		}
		is DynamicExpression -> {
			val lhsEvaluate = dynamic.evaluate
			when (rhs) {
				is ConstantExpression -> {
					val rhsValue = rhs.constant.value
					expression { lhsEvaluate().fn(rhsValue) }
				}
				is DynamicExpression -> {
					val rhsEvaluate = rhs.dynamic.evaluate
					expression { lhsEvaluate().fn(rhsEvaluate()) }
				}
			}
		}
	}

operator fun Expression.invoke(rhs: Expression): Expression =
	doApply(rhs) { (this as (V.() -> V)).invoke(it) }
