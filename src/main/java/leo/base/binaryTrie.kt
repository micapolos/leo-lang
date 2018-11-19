package leo.base

data class BinaryTrie<out V>(
	val binaryMatchOrNullMap: BinaryMap<Match<V>?>) {

	sealed class Match<out V> {
		data class Partial<V>(
			val binaryTrie: BinaryTrie<V>) : Match<V>()

		data class Full<V>(
			val value: V) : Match<V>()
	}
}

fun <V> nullBinaryTrieMatch(): BinaryTrie.Match<V>? =
	null

val <V> BinaryMap<BinaryTrie.Match<V>?>.binaryTrie
	get() =
		BinaryTrie(this)

fun <V> emptyBinaryTrie(): BinaryTrie<V> =
	BinaryTrie(nullBinaryTrieMatch<V>().binaryMap)

fun <V> binaryTrie(zeroMatchOrNull: BinaryTrie.Match<V>?, oneMatchOrNull: BinaryTrie.Match<V>?) =
	binaryMap(zeroMatchOrNull, oneMatchOrNull).binaryTrie

val <V> BinaryTrie<V>.binaryTriePartialMatch: BinaryTrie.Match<V>
	get() =
		BinaryTrie.Match.Partial(this)

val <V> V.binaryTrieFullMatch: BinaryTrie.Match<V>
	get() =
		BinaryTrie.Match.Full(this)

tailrec fun <V> BinaryTrie<V>.match(bitStream: Stream<Bit>): BinaryTrie.Match<V>? {
	val matchOrNull = binaryMatchOrNullMap.get(bitStream.first)
	return if (matchOrNull == null) null
	else {
		val nextBitStreamOrNull = bitStream.nextOrNull
		if (nextBitStreamOrNull == null) matchOrNull
		else when (matchOrNull) {
			is BinaryTrie.Match.Full -> null
			is BinaryTrie.Match.Partial -> matchOrNull.binaryTrie.match(nextBitStreamOrNull)
		}
	}
}

fun <V> BinaryTrie<V>.get(bitStream: Stream<Bit>): The<V>? =
	match(bitStream)?.theValueOrNull

// TODO: Make it tailrec, using accumulator
fun <V> BinaryTrie<V>.set(bitStream: Stream<Bit>, value: V): BinaryTrie<V> {
	val nextBitStreamOrNull: Stream<Bit>? = bitStream.nextOrNull
	return if (nextBitStreamOrNull == null)
		binaryMatchOrNullMap
			.set(bitStream.first, value.binaryTrieFullMatch)
			.binaryTrie
	else {
		val currentMatch = binaryMatchOrNullMap.get(bitStream.first)
		val nextTrie =
			if (currentMatch is BinaryTrie.Match.Partial)
				currentMatch.binaryTrie.set(nextBitStreamOrNull, value)
			else
				emptyBinaryTrie<V>().set(nextBitStreamOrNull, value)
		binaryMatchOrNullMap.set(bitStream.first, nextTrie.binaryTriePartialMatch).binaryTrie
	}
}

val <V> BinaryTrie.Match<V>.theValueOrNull: The<V>?
	get() =
		when (this) {
			is BinaryTrie.Match.Full -> value.the
			is BinaryTrie.Match.Partial -> null
		}