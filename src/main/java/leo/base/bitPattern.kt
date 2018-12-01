package leo.base

import leo.Recurse

data class BitPattern<out V>(
	val opBinaryTrie: BinaryTrie<BitPatternOp<V>>)

sealed class BitPatternOp<out V>

data class BitPatternRecurseOp<out V>(
	val recurse: Recurse) : BitPatternOp<V>()

data class BitPatternValueOp<out V>(
	val value: V) : BitPatternOp<V>()

data class BitPatternApplication<out V>(
	val binaryTrieBackStackOrNull: Stack<BinaryTrie<BitPatternOp<V>>>?,
	val binaryTrieMatch: BinaryTrie.Match<BitPatternOp<V>>)

// === constructors

val <V> BinaryTrie<BitPatternOp<V>>.bitPattern: BitPattern<V>
	get() =
		BitPattern(this)

val <V> BitPattern<V>.application: BitPatternApplication<V>
	get() =
		BitPatternApplication(null, opBinaryTrie.binaryTriePartialMatch)

fun <V> BitPatternApplication<V>.apply(bit: Bit): BitPatternApplication<V>? =
	when (binaryTrieMatch) {
		is BinaryTrie.Match.Full -> null
		is BinaryTrie.Match.Partial -> binaryTrieMatch.get(bit)?.let { nextBinaryTrieMatch ->
			when (nextBinaryTrieMatch) {
				is BinaryTrie.Match.Full -> when (nextBinaryTrieMatch.value) {
					is BitPatternRecurseOp -> TODO()
					is BitPatternValueOp -> nextBinaryTrieMatch
				}
				is BinaryTrie.Match.Partial -> nextBinaryTrieMatch
			}.let { next ->
				BitPatternApplication(
					binaryTrieBackStackOrNull.push(binaryTrieMatch.binaryTrie),
					next)
			}
		}
	}
