package leo32.treo

import leo.base.SeqNode
import leo.base.then

data class Chain(val head: Head, val tail: Tail)

fun chain(head: Head, tail: Tail) = Chain(head, tail)

fun chain(value: Value, vararg values: Value) =
	chain(head(value), tail(*values))

val Chain.valueSeqNode: SeqNode<Value>
	get() =
		head.value then tail.valueSeq
