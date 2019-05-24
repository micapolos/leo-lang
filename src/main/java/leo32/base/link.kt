package leo32.base

import leo.base.Seq
import leo.base.ifOrNull
import leo.base.then
import leo.binary.Bit

data class Link<out V>(
	val bit: Bit,
	val value: V)

fun <V> link(bit: Bit, value: V) = Link(bit, value)
fun <V : Any> Link<V>.at(bit: Bit) = ifOrNull(this.bit == bit) { value }

fun <V> Link<V>.bitSeq(valueBitSeqFn: V.() -> Seq<Bit>): Seq<Bit> =
	Seq { bit.then(value.valueBitSeqFn()) }