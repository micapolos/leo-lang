package leo14.untyped.typed

data class Constant<out T>(val value: T)
data class Dynamic<out T>(val evaluate: () -> T)

sealed class Expression<out T>
data class ConstantExpression<T>(val constant: Constant<T>) : Expression<T>()
data class DynamicExpression<T>(val dynamic: Dynamic<T>) : Expression<T>()

fun <T> constant(value: T) = Constant(value)
fun <T> dynamic(evaluate: () -> T) = Dynamic(evaluate)
val <T> Constant<T>.expression: Expression<T> get() = ConstantExpression(this)
val <T> Dynamic<T>.expression: Expression<T> get() = DynamicExpression(this)
fun <T> expression(value: T): Expression<T> = constant(value).expression
fun <T> expression(fn: () -> T): Expression<T> = dynamic(fn).expression

val <T> Dynamic<T>.value: T
	get() =
		evaluate()

val <T> Expression<T>.value: T
	get() =
		when (this) {
			is ConstantExpression -> constant.value
			is DynamicExpression -> dynamic.value
		}

inline fun <L, O> Expression<L>.doApply(crossinline fn: L.() -> O): Expression<O> =
	when (this) {
		is ConstantExpression -> expression(constant.value.fn())
		is DynamicExpression -> dynamic.evaluate.let { evaluate ->
			expression { evaluate().fn() }
		}
	}

inline fun <L, R, O> Expression<L>.doApply(rhs: Expression<R>, crossinline fn: L.(R) -> O): Expression<O> =
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

operator fun <I, O> Expression<I.() -> O>.invoke(rhs: Expression<I>): Expression<O> =
	doApply(rhs) { invoke(it) }
