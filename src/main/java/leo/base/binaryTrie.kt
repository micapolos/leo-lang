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

fun <V> binaryTrie(vararg pairs: Pair<Stream<Bit>, V>): BinaryTrie<V> =
	emptyBinaryTrie<V>().fold(pairs) { pair -> set(pair.first, pair.second) }

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

val <V> BinaryTrie<V>.pushParser: PushParser<Bit, V>
	get() =
		pushParser { bit ->
			get(bit)?.let { match ->
				when (match) {
					is BinaryTrie.Match.Full -> match.value.doneParser()
					is BinaryTrie.Match.Partial -> match.binaryTrie.pushParser
				}
			}
		}

fun <V> BinaryTrie<V>.get(bit: Bit): BinaryTrie.Match<V>? =
	binaryMatchOrNullMap.get(bit)

fun <V> BinaryTrie.Match<V>.get(bit: Bit): BinaryTrie.Match<V>? =
	when (this) {
		is BinaryTrie.Match.Full -> null
		is BinaryTrie.Match.Partial -> binaryTrie.get(bit)
	}

tailrec fun <V> BinaryTrie<V>.matchOrNull(bitStream: Stream<Bit>): BinaryTrie.Match<V>? {
	val matchOrNull = get(bitStream.first)
	return if (matchOrNull == null) null
	else {
		val nextBitStreamOrNull = bitStream.nextOrNull
		if (nextBitStreamOrNull == null) matchOrNull
		else when (matchOrNull) {
			is BinaryTrie.Match.Full -> null
			is BinaryTrie.Match.Partial -> matchOrNull.binaryTrie.matchOrNull(nextBitStreamOrNull)
		}
	}
}

fun <V> BinaryTrie<V>.get(bitStream: Stream<Bit>): BinaryTrie.Match<V>? =
	matchOrNull(bitStream)

fun <V> BinaryTrie<V>.valueOrNull(bitStream: Stream<Bit>): The<V>? =
	get(bitStream)?.theValueOrNull

fun <V> BinaryTrie<V>.set(bit: Bit, matchOrNull: BinaryTrie.Match<V>?): BinaryTrie<V> =
	binaryMatchOrNullMap.set(bit, matchOrNull).binaryTrie

// TODO: Make it tailrec, using accumulator
fun <V> BinaryTrie<V>.set(bitStream: Stream<Bit>, value: V): BinaryTrie<V> =
	read(bitStream) { bit, nextBitStreamOrNull ->
		if (nextBitStreamOrNull == null) set(bit, value.binaryTrieFullMatch)
		else {
			val currentMatch = get(bit)
			val nextTrie =
				if (currentMatch is BinaryTrie.Match.Partial)
					currentMatch.binaryTrie.set(nextBitStreamOrNull, value)
				else
					emptyBinaryTrie<V>().set(nextBitStreamOrNull, value)
			set(bit, nextTrie.binaryTriePartialMatch)
		}
	}

val <V> BinaryTrie.Match<V>.theValueOrNull: The<V>?
	get() =
		when (this) {
			is BinaryTrie.Match.Full -> value.the
			is BinaryTrie.Match.Partial -> null
		}

fun <V> BinaryTrie<V>.define(bit: Bit, defineNext: BinaryTrie<V>.() -> BinaryTrie.Match<V>?): BinaryTrie<V>? =
	get(bit).let { matchOrNull ->
		if (matchOrNull == null)
			emptyBinaryTrie<V>().defineNext()?.let { nextMatch ->
				set(bit, nextMatch)
			}
		else
			when (matchOrNull) {
				is BinaryTrie.Match.Full -> null
				is BinaryTrie.Match.Partial ->
					matchOrNull.binaryTrie.defineNext()?.let { nextMatch ->
						when (nextMatch) {
							is BinaryTrie.Match.Full -> null
							is BinaryTrie.Match.Partial -> set(bit, nextMatch)
						}
					}
			}
	}

fun <V> BinaryTrie<V>.defineBit(bitStream: Stream<Bit>, defineNext: BinaryTrie<V>.() -> BinaryTrie.Match<V>?): BinaryTrie<V>? =
	define(bitStream.first) {
		bitStream.nextOrNull.let { nextBitStreamOrNull ->
			if (nextBitStreamOrNull == null) defineNext()
			else defineBit(nextBitStreamOrNull, defineNext)?.binaryTriePartialMatch
		}
	}
