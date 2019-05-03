package leo32.treo

data class Chain(val head: Head, val tail: Tail)

fun chain(head: Head, tail: Tail) = Chain(head, tail)

fun chain(value: Value, vararg values: Value) =
	chain(head(value), tail(*values))
