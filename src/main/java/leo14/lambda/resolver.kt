package leo14.lambda

typealias NativeApply<T> = T.(Value<T>) -> Value<T>?

data class Resolver<T>(val fn: T.(Value<T>) -> Value<T>?)

fun <T> Resolver<T>.resolve(lhs: T, rhs: Value<T>): Value<T>? = lhs.fn(rhs)

fun <T> resolver(fn: T.(Value<T>) -> Value<T>?) = Resolver(fn)

fun <T> nullResolver(): Resolver<T> = resolver { error("$this.resolve($it)") }

fun <T> errorNativeApply(): NativeApply<T> = { error("nativeApply") }

