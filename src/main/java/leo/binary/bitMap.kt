package leo.binary

import leo.base.Bit
import leo.base.ifNull

data class BitMap<out V>(
	val bit0ValueOrNull: V,
	val bit1ValueOrNull: V)

fun <V : Any> emptyBitMap(): BitMap<V?> =
	BitMap<V?>(null, null)

fun <V : Any> bitMap(bit: Bit, value: V): BitMap<V?> =
	when (bit) {
		Bit.ZERO -> BitMap(value, null)
		Bit.ONE -> BitMap(null, value)
	}

fun <V> BitMap<V>.get(bit: Bit): V? =
	when (bit) {
		Bit.ZERO -> bit0ValueOrNull
		Bit.ONE -> bit1ValueOrNull
	}

fun <V> BitMap<V>.set(bit: Bit, value: V): BitMap<V> =
	when (bit) {
		Bit.ZERO -> copy(bit0ValueOrNull = value)
		Bit.ONE -> copy(bit1ValueOrNull = value)
	}

fun <V> BitMap<V>.plus(bit: Bit, value: V): BitMap<V>? =
	when (bit) {
		Bit.ZERO -> bit0ValueOrNull.ifNull { copy(bit0ValueOrNull = value) }
		Bit.ONE -> bit1ValueOrNull.ifNull { copy(bit1ValueOrNull = value) }
	}
