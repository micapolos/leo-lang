package leo14.lambda

typealias NativeApply<T> = T.(Value<T>) -> Value<T>?

data class Evaluator<T>(val fn: T.(Value<T>) -> Value<T>?)

fun <T> Evaluator<T>.resolve(lhs: T, rhs: Value<T>): Value<T>? = lhs.fn(rhs)

fun <T> evaluator(fn: T.(Value<T>) -> Value<T>?) = Evaluator(fn)

fun <T> nullEvaluator(): Evaluator<T> = evaluator { error("$this.resolve($it)") }

fun <T> errorNativeApply(): NativeApply<T> = { error("nativeApply") }

