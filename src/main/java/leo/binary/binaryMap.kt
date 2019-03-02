package leo.binary

import leo.base.Bit

data class BinaryMap<out V>(
	val matchBitMap: BitMap<BinaryMapMatch<V>?>)

sealed class BinaryMapMatch<out V>

data class BinaryMapFullMatch<out V>(
	val value: V) : BinaryMapMatch<V>()

data class BinaryMapPartialMatch<out V>(
	val binaryMap: BinaryMap<V>) : BinaryMapMatch<V>()

// === empty

fun <V> emptyBinaryMap() =
	BinaryMap<V>(emptyBitMap())

// === singleton

fun <V> binaryMap(bit: Bit, match: BinaryMapMatch<V>): BinaryMap<V> =
	BinaryMap(bitMap(bit, match))

fun <V> binaryMap(bit: Bit, value: V): BinaryMap<V> =
	BinaryMap(bitMap(bit, BinaryMapFullMatch(value)))

fun <V> binaryMap(binary: Binary, match: BinaryMapMatch<V>): BinaryMap<V> =
	if (binary.tailBinaryOrNull == null) binaryMap(binary.headBit, match)
	else BinaryMap(bitMap(binary.headBit, BinaryMapPartialMatch(binaryMap(binary.tailBinaryOrNull, match))))

fun <V> binaryMap(binary: Binary, value: V): BinaryMap<V> =
	binaryMap(binary, BinaryMapFullMatch(value))

// === access

fun <V> BinaryMap<V>.matchOrNull(bit: Bit) =
	matchBitMap.get(bit)

fun <V> BinaryMap<V>.matchOrNull(binary: Binary): BinaryMapMatch<V>? =
	if (binary.tailBinaryOrNull == null) matchOrNull(binary.headBit)
	else matchOrNull(binary.headBit)
		?.binaryMapOrNull
		?.run { matchOrNull(binary.tailBinaryOrNull) }

fun <V> BinaryMap<V>.get(bit: Bit): V? =
	matchOrNull(bit)?.valueOrNull

fun <V> BinaryMap<V>.get(binary: Binary): V? =
	matchOrNull(binary)?.valueOrNull

// === set

fun <V> BinaryMap<V>.set(bit: Bit, match: BinaryMapMatch<V>): BinaryMap<V> =
	BinaryMap(matchBitMap.set(bit, match))

fun <V> BinaryMap<V>.plus(bit: Bit, match: BinaryMapMatch<V>): BinaryMap<V>? =
	matchBitMap.plus(bit, match)?.run(::BinaryMap)

fun <V> BinaryMap<V>.plus(binary: Binary, match: BinaryMapMatch<V>): BinaryMap<V>? =
	matchBitMap.get(binary.headBit).let { matchOrNull ->
		if (matchOrNull == null)
			matchBitMap.set(
				binary.headBit,
				if (binary.tailBinaryOrNull == null) match
				else BinaryMapPartialMatch(binaryMap(binary.tailBinaryOrNull, match)))
		else
			if (binary.tailBinaryOrNull == null) null
			else matchOrNull.plus(binary.tailBinaryOrNull, match)?.let { matchBitMap.set(binary.headBit, it) }
	}?.run(::BinaryMap)

fun <V> BinaryMap<V>.plus(binary: Binary, value: V): BinaryMap<V>? =
	plus(binary, BinaryMapFullMatch(value))

fun <V> binaryMapMatch(binary: Binary, match: BinaryMapMatch<V>): BinaryMapMatch<V> =
	BinaryMapPartialMatch(binaryMap(binary, match))

val <V> BinaryMapMatch<V>.valueOrNull: V?
	get() =
		when (this) {
			is BinaryMapPartialMatch -> null
			is BinaryMapFullMatch -> value
		}

val <V> BinaryMapMatch<V>.binaryMapOrNull: BinaryMap<V>?
	get() =
		when (this) {
			is BinaryMapPartialMatch -> binaryMap
			is BinaryMapFullMatch -> null
		}

fun <V> BinaryMapMatch<V>.plus(binary: Binary, match: BinaryMapMatch<V>): BinaryMapMatch<V>? =
	when (this) {
		is BinaryMapPartialMatch -> binaryMap.plus(binary, match)?.run(::BinaryMapPartialMatch)
		is BinaryMapFullMatch -> null
	}
