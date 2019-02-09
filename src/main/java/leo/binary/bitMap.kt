package leo.binary

import leo.base.ifNull

data class BitMap<out V>(
	val bit0ValueOrNull: V?,
	val bit1ValueOrNull: V?)

fun <V> emptyBitMap() =
	BitMap<V>(null, null)

fun <V> bitMap(bit: Bit, value: V): BitMap<V> =
	when (bit) {
		Bit0 -> BitMap(value, null)
		Bit1 -> BitMap(null, value)
	}

fun <V> BitMap<V>.get(bit: Bit): V? =
	when (bit) {
		Bit0 -> bit0ValueOrNull
		Bit1 -> bit1ValueOrNull
	}

fun <V> BitMap<V>.set(bit: Bit, value: V?): BitMap<V> =
	when (bit) {
		Bit0 -> copy(bit0ValueOrNull = value)
		Bit1 -> copy(bit1ValueOrNull = value)
	}

fun <V> BitMap<V>.update(bit: Bit, updateFn: (V?) -> V?): BitMap<V> =
	set(bit, updateFn(get(bit)))

fun <V> BitMap<V>.plus(bit: Bit, value: V): BitMap<V>? =
	when (bit) {
		Bit0 -> bit0ValueOrNull.ifNull { copy(bit0ValueOrNull = value) }
		Bit1 -> bit1ValueOrNull.ifNull { copy(bit1ValueOrNull = value) }
	}
