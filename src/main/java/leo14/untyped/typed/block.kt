package leo14.untyped.typed

data class Constant<out T>(val value: T)
data class Dynamic<out T>(val evaluate: () -> T)

sealed class Block<out T>
data class ConstantBlock<T>(val constant: Constant<T>) : Block<T>()
data class DynamicBlock<T>(val dynamic: Dynamic<T>) : Block<T>()

fun <T> constant(value: T) = Constant(value)
fun <T> dynamic(evaluate: () -> T) = Dynamic(evaluate)
val <T> Constant<T>.block: Block<T> get() = ConstantBlock(this)
val <T> Dynamic<T>.block: Block<T> get() = DynamicBlock(this)

inline fun <L, O> Block<L>.doApply(crossinline fn: L.() -> O): Block<O> =
	when (this) {
		is ConstantBlock -> constant(constant.value.fn()).block
		is DynamicBlock -> dynamic.evaluate.let { evaluate ->
			dynamic { evaluate().fn() }.block
		}
	}

inline fun <L, R, O> Block<L>.doApply(rhs: Block<R>, crossinline fn: L.(R) -> O): Block<O> =
	when (this) {
		is ConstantBlock -> {
			val lhsValue = constant.value
			when (rhs) {
				is ConstantBlock -> {
					val rhsValue = rhs.constant.value
					constant(lhsValue.fn(rhsValue)).block
				}
				is DynamicBlock -> {
					val rhsEvaluate = rhs.dynamic.evaluate
					dynamic { lhsValue.fn(rhsEvaluate()) }.block
				}
			}
		}
		is DynamicBlock -> {
			val lhsEvaluate = dynamic.evaluate
			when (rhs) {
				is ConstantBlock -> {
					val rhsValue = rhs.constant.value
					dynamic { lhsEvaluate().fn(rhsValue) }.block
				}
				is DynamicBlock -> {
					val rhsEvaluate = rhs.dynamic.evaluate
					dynamic { lhsEvaluate().fn(rhsEvaluate()) }.block
				}
			}
		}
	}

val <T> Dynamic<T>.value: T get() = evaluate()

val <T> Block<T>.value: T
	get() =
		when (this) {
			is ConstantBlock -> constant.value
			is DynamicBlock -> dynamic.value
		}
