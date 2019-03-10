package leo.base

data class BitPattern<out V>(
	val opBinaryTrie: BinaryTrie<BitPatternOp<V>>)

sealed class BitPatternOp<out V>

data class BitPatternRecurseOp<out V>(
	val offset: Int) : BitPatternOp<V>()

data class BitPatternDoneOp<out V>(
	val value: V) : BitPatternOp<V>()

data class BitPatternMatch<out V>(
	val opBinaryTrieMatch: BinaryTrie.Match<BitPatternOp<V>>)

// === constructors

val <V> BinaryTrie<BitPatternOp<V>>.bitPattern: BitPattern<V>
	get() =
		BitPattern(this)

fun <V> bitPattern(zeroMatchOrNull: BitPatternMatch<V>?, oneMatchOrNull: BitPatternMatch<V>?): BitPattern<V> =
	binaryTrie(zeroMatchOrNull?.opBinaryTrieMatch, oneMatchOrNull?.opBinaryTrieMatch).bitPattern

val <V> BinaryTrie.Match<BitPatternOp<V>>.bitPatternMatch: BitPatternMatch<V>
	get() =
		BitPatternMatch(this)

fun <V> bitPatternRecurseMatch(offset: Int): BitPatternMatch<V> =
	offset.bitPatternRecurseOp<V>().binaryTrieFullMatch.bitPatternMatch

fun <V> bitPatternMatch(value: V): BitPatternMatch<V> =
	value.bitPatternDoneOp.binaryTrieFullMatch.bitPatternMatch

fun <V> match(bitPattern: BitPattern<V>): BitPatternMatch<V> =
	bitPattern.opBinaryTrie.binaryTriePartialMatch.bitPatternMatch

fun <V> Int.bitPatternRecurseOp(): BitPatternOp<V> =
	BitPatternRecurseOp(this)

val <V> V.bitPatternDoneOp: BitPatternOp<V>
	get() =
		BitPatternDoneOp(this)

// === push parser

val <V> BitPattern<V>.pushParser: PushParser<EnumBit, V>
	get() =
		pushParser(null)

fun <V> BitPattern<V>.pushParser(traceStackOrNull: Stack<BitPattern<V>>?): PushParser<EnumBit, V> =
	pushParser { bit ->
		opBinaryTrie
			.get(bit)
			?.let { binaryTrieMatch ->
				when (binaryTrieMatch) {
					is BinaryTrie.Match.Partial ->
						binaryTrieMatch.binaryTrie.bitPattern.pushParser(traceStackOrNull.push(this))
					is BinaryTrie.Match.Full ->
						when (binaryTrieMatch.value) {
							is BitPatternRecurseOp ->
								traceStackOrNull.push(this).let { traceStack ->
									traceStack
										.pop(binaryTrieMatch.value.offset)
										?.let { poppedTraceStack ->
											poppedTraceStack.head.pushParser(poppedTraceStack.tail)
										}
								}
							is BitPatternDoneOp ->
								binaryTrieMatch.value.value.doneParser()
						}
				}
			}
	}
