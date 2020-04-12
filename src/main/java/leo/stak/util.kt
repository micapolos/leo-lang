package leo.stak

import leo13.push
import leo13.reverse
import leo13.toReverseList

fun <R, T : Any> R.fold(stak: Stak<T>, fn: R.(T) -> R): R =
	if (stak.nodeOrNull == null) this
	else fold(stak.nodeOrNull, fn)

val <T : Any> Stak<T>.reverse
	get() =
		stakOf<T>().fold(this) { push(it) }

val <T : Any> Stak<T>.reverseStack
	get() =
		leo13.stack<T>().fold(this) { push(it) }

val <T : Any> Stak<T>.stack
	get() =
		reverseStack.reverse

val <T : Any> Stak<T>.list
	get() =
		reverseStack.toReverseList()

fun <R : Any, T : Any> Pair<R, Stak<T>>.reduce(fn: R.(T) -> R): Pair<R, Stak<T>>? =
	second.unlink?.let { (stak, value) ->
		first.fn(value) to stak
	}
