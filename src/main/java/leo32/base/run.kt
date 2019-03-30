package leo32.base

data class Effect<out T, R>(
	val target: T,
	val value: R)

fun <T, R> T.effect(value: R) =
	Effect(this, value)

fun <T1, T2, R> Effect<T1, R>.mapTarget(fn: T1.() -> T2) =
	Effect(fn(target), value)