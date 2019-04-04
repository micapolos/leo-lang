package leo32.base

data class Effect<out T, R>(
	val target: T,
	val value: R)

fun <T, R> T.effect(value: R) =
	Effect(this, value)

fun <T1, T2, R> Effect<T1, R>.mapTarget(fn: T1.() -> T2) =
	Effect(fn(target), value)

fun <T, V, R> Effect<T, V>.apply(fn: T.(V) -> R) =
	target.fn(value)

//operator fun <T, R> Effect<T, R>.component1() = target
//operator fun <T, R> Effect<T, R>.component2() = target