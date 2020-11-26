package leo23.typed

data class Typed<out V, out T>(val v: V, val t: T)

fun <V, T> V.of(t: T) = Typed(this, t)