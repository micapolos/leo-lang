package leo.binary

import leo.base.ifNull

data class Choice<out V>(
	val atBitZero: V,
	val atBitOne: V)

fun <V : Any> emptyChoice(): Choice<V?> =
	Choice<V?>(null, null)

fun <V : Any> array(bit: Bit, value: V): Choice<V?> =
	when (bit) {
		Bit.ZERO -> Choice(value, null)
		Bit.ONE -> Choice(null, value)
	}

fun <V> Choice<V>.get(bit: Bit): V =
	when (bit) {
		Bit.ZERO -> atBitZero
		Bit.ONE -> atBitOne
	}

fun <V> Choice<V>.set(bit: Bit, value: V): Choice<V> =
	when (bit) {
		Bit.ZERO -> copy(atBitZero = value)
		Bit.ONE -> copy(atBitOne = value)
	}

fun <V> Choice<V>.plus(bit: Bit, value: V): Choice<V>? =
	when (bit) {
		Bit.ZERO -> atBitZero.ifNull { copy(atBitZero = value) }
		Bit.ONE -> atBitOne.ifNull { copy(atBitOne = value) }
	}
