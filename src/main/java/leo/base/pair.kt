package leo.base

fun <A, B> A.pairTo(b: B): Pair<A, B> = to(b)

inline fun <A, B, C> Pair<A, B>.pairBind(fn: A.(B) -> C): C =
	first.fn(second)

fun <A, B> A.pairReturn(second: B): Pair<A, B> =
	this to second
