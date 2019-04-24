package leo.base

data class IfTrue<out V>(val condition: Boolean, val thenFn: () -> V)

fun <V> Boolean.ifTrue(fn: () -> V) = IfTrue(this, fn)
fun <V> IfTrue<V>.orElse(elseFn: () -> V) = if (condition) thenFn() else elseFn()
