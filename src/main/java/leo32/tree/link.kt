package leo32.tree

import leo.base.ifOrNull
import leo.binary.Bit

data class Link<out V>(
	val bit: Bit,
	val value: V)

fun <V> link(bit: Bit, value: V) = Link(bit, value)
fun <V : Any> Link<V>.at(bit: Bit) = ifOrNull(this.bit == bit) { value }