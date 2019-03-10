package leo.base

import kotlin.test.Test

class BinaryTrieTest {
	val emptyTrie = emptyBinaryTrie<String>()

	val match0 = "0".binaryTrieFullMatch
	val match100 = "100".binaryTrieFullMatch
	val match101 = "101".binaryTrieFullMatch
	val match11 = "11".binaryTrieFullMatch
	val trie10 = binaryTrie(match100, match101)
	val match10 = trie10.binaryTriePartialMatch
	val trie1 = binaryTrie(match10, match11)
	val match1 = trie1.binaryTriePartialMatch
	val testTrie = binaryTrie(match0, match1)

	@Test
	fun get() {
		testTrie.pushParser.push(0.enumBit)?.parsedOrNull.assertEqualTo("0")
		testTrie.pushParser.push(1.enumBit)?.parsedOrNull.assertEqualTo(null)
		testTrie.pushParser.push(0.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo(null)
		testTrie.pushParser.push(0.enumBit)?.push(1.enumBit)?.parsedOrNull.assertEqualTo(null)
		testTrie.pushParser.push(1.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo(null)
		testTrie.pushParser.push(1.enumBit)?.push(1.enumBit)?.parsedOrNull.assertEqualTo("11")
		testTrie.pushParser.push(0.enumBit)?.push(0.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo(null)
		testTrie.pushParser.push(0.enumBit)?.push(0.enumBit)?.push(1.enumBit)?.parsedOrNull.assertEqualTo(null)
		testTrie.pushParser.push(0.enumBit)?.push(1.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo(null)
		testTrie.pushParser.push(0.enumBit)?.push(1.enumBit)?.push(1.enumBit)?.parsedOrNull.assertEqualTo(null)
		testTrie.pushParser.push(1.enumBit)?.push(0.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo("100")
		testTrie.pushParser.push(1.enumBit)?.push(0.enumBit)?.push(1.enumBit)?.parsedOrNull.assertEqualTo("101")
		testTrie.pushParser.push(1.enumBit)?.push(1.enumBit)?.push(0.enumBit)?.parsedOrNull.assertEqualTo(null)
		testTrie.pushParser.push(1.enumBit)?.push(1.enumBit)?.push(1.enumBit)?.parsedOrNull.assertEqualTo(null)
	}

	@Test
	fun set() {
		val newTrie = testTrie.set(stream(EnumBit.ONE, EnumBit.ZERO), "x10")
		newTrie.valueOrNull(stream(EnumBit.ZERO)).assertEqualTo("0".the)
		newTrie.valueOrNull(stream(EnumBit.ONE)).assertEqualTo(null)
		newTrie.valueOrNull(stream(EnumBit.ZERO, EnumBit.ZERO)).assertEqualTo(null)
		newTrie.valueOrNull(stream(EnumBit.ZERO, EnumBit.ONE)).assertEqualTo(null)
		newTrie.valueOrNull(stream(EnumBit.ONE, EnumBit.ZERO)).assertEqualTo("x10".the)
		newTrie.valueOrNull(stream(EnumBit.ONE, EnumBit.ONE)).assertEqualTo("11".the)
		newTrie.valueOrNull(stream(EnumBit.ZERO, EnumBit.ZERO, EnumBit.ZERO)).assertEqualTo(null)
		newTrie.valueOrNull(stream(EnumBit.ZERO, EnumBit.ZERO, EnumBit.ONE)).assertEqualTo(null)
		newTrie.valueOrNull(stream(EnumBit.ZERO, EnumBit.ONE, EnumBit.ZERO)).assertEqualTo(null)
		newTrie.valueOrNull(stream(EnumBit.ZERO, EnumBit.ONE, EnumBit.ONE)).assertEqualTo(null)
		newTrie.valueOrNull(stream(EnumBit.ONE, EnumBit.ZERO, EnumBit.ZERO)).assertEqualTo(null)
		newTrie.valueOrNull(stream(EnumBit.ONE, EnumBit.ZERO, EnumBit.ONE)).assertEqualTo(null)
		newTrie.valueOrNull(stream(EnumBit.ONE, EnumBit.ONE, EnumBit.ZERO)).assertEqualTo(null)
		newTrie.valueOrNull(stream(EnumBit.ONE, EnumBit.ONE, EnumBit.ONE)).assertEqualTo(null)
	}

	@Test
	fun defineSingle() {
		emptyTrie
			.define(EnumBit.ZERO) { "0".binaryTrieFullMatch }
			.assertEqualTo(emptyTrie.set(EnumBit.ZERO, "0".binaryTrieFullMatch))
	}

	@Test
	fun defineAlternatives() {
		emptyTrie
			.define(EnumBit.ZERO) { "0".binaryTrieFullMatch }!!
			.define(EnumBit.ONE) { "1".binaryTrieFullMatch }
			.assertEqualTo(
				emptyTrie
					.set(EnumBit.ZERO, "0".binaryTrieFullMatch)
					.set(EnumBit.ONE, "1".binaryTrieFullMatch))
	}

	@Test
	fun defineSequence() {
		emptyTrie
			.define(EnumBit.ZERO) {
				define(EnumBit.ZERO) {
					"00".binaryTrieFullMatch
				}!!.binaryTriePartialMatch
			}
			.assertEqualTo(
				emptyTrie
					.set(EnumBit.ZERO,
						emptyTrie.set(EnumBit.ZERO,
							"00".binaryTrieFullMatch).binaryTriePartialMatch))
	}

	@Test
	fun defineSingleThenAlternatives() {
		emptyTrie
			.define(EnumBit.ZERO) {
				define(EnumBit.ZERO) {
					"00".binaryTrieFullMatch
				}!!.binaryTriePartialMatch
			}!!
			.define(EnumBit.ZERO) {
				define(EnumBit.ONE) {
					"01".binaryTrieFullMatch
				}!!.binaryTriePartialMatch
			}
			.assertEqualTo(
				emptyTrie
					.set(EnumBit.ZERO,
						emptyTrie
							.set(EnumBit.ZERO, "00".binaryTrieFullMatch)
							.set(EnumBit.ONE, "01".binaryTrieFullMatch)
							.binaryTriePartialMatch))
	}

	@Test
	fun defineBitStream() {
		emptyTrie
			.defineBit(stream(EnumBit.ZERO, EnumBit.ONE)) {
				"01".binaryTrieFullMatch
			}
			.assertEqualTo(
				emptyTrie
					.set(EnumBit.ZERO,
						emptyTrie
							.set(EnumBit.ONE, "01".binaryTrieFullMatch)
							.binaryTriePartialMatch))
	}

	@Test
	fun defineConflict() {
		emptyTrie
			.define(EnumBit.ZERO) { "0".binaryTrieFullMatch }!!
			.define(EnumBit.ZERO) { "00".binaryTrieFullMatch }
			.assertEqualTo(null)
	}
}