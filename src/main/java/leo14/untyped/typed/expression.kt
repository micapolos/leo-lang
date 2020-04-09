@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo14.minus
import leo14.plus
import leo14.times
import leo14.unaryMinus

data class Constant(val value: Value)
data class Dynamic(val evaluate: Evaluate)

sealed class Expression
data class ConstantExpression(val constant: Constant) : Expression()
data class DynamicExpression(val dynamic: Dynamic) : Expression()

fun constant(value: Value) = Constant(value)
fun dynamic(evaluate: Evaluate) = Dynamic(evaluate)
val Constant.expression: Expression get() = ConstantExpression(this)
val Dynamic.expression: Expression get() = DynamicExpression(this)
fun expression(value: Value): Expression = constant(value).expression
fun expression(evaluate: Evaluate): Expression = dynamic(evaluate).expression

val Dynamic.value: Value
	get() =
		evaluate()

val Expression.value: Value
	get() =
		when (this) {
			is ConstantExpression -> constant.value
			is DynamicExpression -> dynamic.value
		}

val Expression.evaluate: Expression
	get() =
		expression(value)

inline fun Expression.doApply(crossinline fn: Value.() -> Value): Expression =
	when (this) {
		is ConstantExpression -> expression(constant.value.fn())
		is DynamicExpression -> dynamic.evaluate.let { evaluate ->
			expression { evaluate().fn() }
		}
	}

inline fun Expression.doApply(rhs: Expression, crossinline fn: Value.(Value) -> Value): Expression =
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
	doApply(rhs) { asFn.invoke(it) }

fun Expression.stringPlusString(rhs: Expression): Expression =
	doApply(rhs) { asString + it.asString }

val Expression.numberUnaryMinus: Expression
	get() =
		doApply { -asNumber }

fun Expression.numberPlusNumber(rhs: Expression): Expression =
	doApply(rhs) { asNumber + it.asNumber }

fun Expression.numberMinusNumber(rhs: Expression): Expression =
	doApply(rhs) { asNumber - asNumber }

fun Expression.numberTimesNumber(rhs: Expression): Expression =
	doApply(rhs) { asNumber * it.asNumber }

val Expression.numberString: Expression
	get() =
		doApply { asNumber.toString() }
