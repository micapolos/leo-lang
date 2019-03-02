package leo.base

data class FirstThen<A, B>(
	val first: A,
	val then: B)

infix fun <A, B> A.then(then: B): FirstThen<A, B> =
	FirstThen(this, then)
