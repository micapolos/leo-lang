package leo.base

data class Linked<out T>(val value: T, val to: Linked<T>?)

infix fun <T> Linked<T>?.from(value: T) =
	Linked(value, this)

fun <T> linked(vararg values: T) =
	values.foldRight(nullOf<Linked<T>>()) { value, linked -> linked from value }

tailrec fun <R, T> R.fold(linked: Linked<T>?, fn: R.(T) -> R): R =
	if (linked == null) this else fn(linked.value).fold(linked.to, fn)

val <T> Linked<T>?.reverse
	get() =
		linked<T>().fold(this) { from(it) }
