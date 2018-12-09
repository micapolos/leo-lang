package leo.term

data class Left<out V>(
	val operand: Operand<V>)

val <V> Operand<V>.left: Left<V>
	get() =
		Left(this)

fun <V> Appendable.append(left: Left<V>): Appendable =
	append(left.operand)

fun <V, R> Left<V>.map(fn: V.() -> R): Left<R> =
	operand.map(fn).left