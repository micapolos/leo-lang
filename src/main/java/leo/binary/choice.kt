package leo.binary

import leo.base.ifNull

data class Choice<out V>(
	val atBitZero: V,
	val atBitOne: V)

fun <V : Any> emptyChoice(): Choice<V?> =
	Choice<V?>(null, null)

val <V> V.allChoice
	get() =
		Choice(this, this)

fun <V : Any> array(bit: Bit, value: V): Choice<V?> =
	bit.match(
		{ Choice(value, null) },
		{ Choice(null, value) })

fun <V> Choice<V>.get(bit: Bit): V =
	bit.match(
		{ atBitZero },
		{ atBitOne })

fun <V> Choice<V>.set(bit: Bit, value: V): Choice<V> =
	bit.match(
		{ copy(atBitZero = value) },
		{ copy(atBitOne = value) })

fun <V> Choice<V>.plus(bit: Bit, value: V): Choice<V>? =
	bit.match(
		{ atBitZero.ifNull { copy(atBitZero = value) } },
		{ atBitOne.ifNull { copy(atBitOne = value) } })
