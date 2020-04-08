package leo.base

class Fix<T>(val fn: Fix<T>.(T) -> T)

fun <T> fix(fn: Fix<T>.(T) -> T) = Fix(fn)
operator fun <T> Fix<T>.invoke(value: T) = fn(value)
