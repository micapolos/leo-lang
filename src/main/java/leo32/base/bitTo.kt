package leo32.base

import leo.base.notNullIf
import leo.binary.Bit

data class BitTo<V : Any>(
	val bit: Bit,
	val value: V)

infix fun <V : Any> Bit.to(value: V) =
	BitTo(this, value)

fun <V : Any> BitTo<V>.at(bit: Bit): V? =
	notNullIf(this.bit == bit) { value }