package leo.term

data class Right<out V>(
	val operand: Operand<V>)

val <V> Operand<V>.right: Right<V>
	get() =
		Right(this)

fun <V> Appendable.append(right: Right<V>): Appendable =
	append(right.operand)

fun <V, R> Right<V>.map(fn: V.() -> R): Right<R> =
	operand.map(fn).right

val <V> Right<V>.isSimple: Boolean
	get() =
		operand.term.isSimple

