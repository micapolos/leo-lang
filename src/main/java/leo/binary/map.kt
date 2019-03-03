package leo.binary

data class Map<out V>(
	val matchChoice: Choice<MapMatch<V>?>)

sealed class MapMatch<out V>

data class MapFullMatch<out V>(
	val value: V) : MapMatch<V>()

data class MapPartialMatch<out V>(
	val map: Map<V>) : MapMatch<V>()

// === empty

fun <V> emptyBinaryMap() =
	Map<V>(emptyChoice())

// === singleton

fun <V> map(bit: Bit, match: MapMatch<V>): Map<V> =
	Map(array(bit, match))

fun <V> map(bit: Bit, value: V): Map<V> =
	Map(array(bit, MapFullMatch(value)))

fun <V> map(binary: Binary, match: MapMatch<V>): Map<V> =
	if (binary.tailBinaryOrNull == null) map(binary.headBit, match)
	else Map(array(binary.headBit, MapPartialMatch(map(binary.tailBinaryOrNull, match))))

fun <V> map(binary: Binary, value: V): Map<V> =
	map(binary, MapFullMatch(value))

// === access

fun <V> Map<V>.matchOrNull(bit: Bit) =
	matchChoice.get(bit)

fun <V> Map<V>.matchOrNull(binary: Binary): MapMatch<V>? =
	if (binary.tailBinaryOrNull == null) matchOrNull(binary.headBit)
	else matchOrNull(binary.headBit)
		?.mapOrNull
		?.run { matchOrNull(binary.tailBinaryOrNull) }

fun <V> Map<V>.get(bit: Bit): V? =
	matchOrNull(bit)?.valueOrNull

fun <V> Map<V>.get(binary: Binary): V? =
	matchOrNull(binary)?.valueOrNull

// === set

fun <V> Map<V>.set(bit: Bit, match: MapMatch<V>): Map<V> =
	Map(matchChoice.set(bit, match))

fun <V> Map<V>.plus(bit: Bit, match: MapMatch<V>): Map<V>? =
	matchChoice.plus(bit, match)?.run(::Map)

fun <V> Map<V>.plus(binary: Binary, match: MapMatch<V>): Map<V>? =
	matchChoice.get(binary.headBit).let { matchOrNull ->
		if (matchOrNull == null)
			matchChoice.set(
				binary.headBit,
				if (binary.tailBinaryOrNull == null) match
				else MapPartialMatch(map(binary.tailBinaryOrNull, match)))
		else
			if (binary.tailBinaryOrNull == null) null
			else matchOrNull.plus(binary.tailBinaryOrNull, match)?.let { matchChoice.set(binary.headBit, it) }
	}?.run(::Map)

fun <V> Map<V>.plus(binary: Binary, value: V): Map<V>? =
	plus(binary, MapFullMatch(value))

fun <V> mapMatch(binary: Binary, match: MapMatch<V>): MapMatch<V> =
	MapPartialMatch(map(binary, match))

val <V> MapMatch<V>.valueOrNull: V?
	get() =
		when (this) {
			is MapPartialMatch -> null
			is MapFullMatch -> value
		}

val <V> MapMatch<V>.mapOrNull: Map<V>?
	get() =
		when (this) {
			is MapPartialMatch -> map
			is MapFullMatch -> null
		}

fun <V> MapMatch<V>.plus(binary: Binary, match: MapMatch<V>): MapMatch<V>? =
	when (this) {
		is MapPartialMatch -> map.plus(binary, match)?.run(::MapPartialMatch)
		is MapFullMatch -> null
	}
