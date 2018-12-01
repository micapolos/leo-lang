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
		testTrie.application.get(0.bit)
			.assertEqualTo(stack(testTrie).and(match0))
		testTrie.application.get(1.bit)
			.assertEqualTo(stack(testTrie).and(match1))
		testTrie.application.get(0.bit)?.get(0.bit)
			.assertEqualTo(null)
		testTrie.application.get(0.bit)?.get(1.bit)
			.assertEqualTo(null)
		testTrie.application.get(1.bit)?.get(0.bit)
			.assertEqualTo(stack(testTrie, trie1).and(match10))
		testTrie.application.get(1.bit)?.get(1.bit)
			.assertEqualTo(stack(testTrie, trie1).and(match11))
		testTrie.application.get(0.bit)?.get(0.bit)?.get(0.bit)
			.assertEqualTo(null)
		testTrie.application.get(0.bit)?.get(0.bit)?.get(1.bit)
			.assertEqualTo(null)
		testTrie.application.get(0.bit)?.get(1.bit)?.get(0.bit)
			.assertEqualTo(null)
		testTrie.application.get(0.bit)?.get(1.bit)?.get(1.bit)
			.assertEqualTo(null)
		testTrie.application.get(1.bit)?.get(0.bit)?.get(0.bit)
			.assertEqualTo(stack(testTrie, trie1, trie10).and(match100))
		testTrie.application.get(1.bit)?.get(0.bit)?.get(1.bit)
			.assertEqualTo(stack(testTrie, trie1, trie10).and(match101))
		testTrie.application.get(1.bit)?.get(1.bit)?.get(0.bit)
			.assertEqualTo(null)
		testTrie.application.get(1.bit)?.get(1.bit)?.get(1.bit)
			.assertEqualTo(null)
	}

	@Test
	fun set() {
		val newTrie = testTrie.set(stream(Bit.ONE, Bit.ZERO), "x10")
		newTrie.valueOrNull(stream(Bit.ZERO)).assertEqualTo("0".the)
		newTrie.valueOrNull(stream(Bit.ONE)).assertEqualTo(null)
		newTrie.valueOrNull(stream(Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		newTrie.valueOrNull(stream(Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		newTrie.valueOrNull(stream(Bit.ONE, Bit.ZERO)).assertEqualTo("x10".the)
		newTrie.valueOrNull(stream(Bit.ONE, Bit.ONE)).assertEqualTo("11".the)
		newTrie.valueOrNull(stream(Bit.ZERO, Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		newTrie.valueOrNull(stream(Bit.ZERO, Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		newTrie.valueOrNull(stream(Bit.ZERO, Bit.ONE, Bit.ZERO)).assertEqualTo(null)
		newTrie.valueOrNull(stream(Bit.ZERO, Bit.ONE, Bit.ONE)).assertEqualTo(null)
		newTrie.valueOrNull(stream(Bit.ONE, Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		newTrie.valueOrNull(stream(Bit.ONE, Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		newTrie.valueOrNull(stream(Bit.ONE, Bit.ONE, Bit.ZERO)).assertEqualTo(null)
		newTrie.valueOrNull(stream(Bit.ONE, Bit.ONE, Bit.ONE)).assertEqualTo(null)
	}

	@Test
	fun defineSingle() {
		emptyTrie
			.define(Bit.ZERO) { "0".binaryTrieFullMatch }
			.assertEqualTo(emptyTrie.set(Bit.ZERO, "0".binaryTrieFullMatch))
	}

	@Test
	fun defineAlternatives() {
		emptyTrie
			.define(Bit.ZERO) { "0".binaryTrieFullMatch }!!
			.define(Bit.ONE) { "1".binaryTrieFullMatch }
			.assertEqualTo(
				emptyTrie
					.set(Bit.ZERO, "0".binaryTrieFullMatch)
					.set(Bit.ONE, "1".binaryTrieFullMatch))
	}

	@Test
	fun defineSequence() {
		emptyTrie
			.define(Bit.ZERO) {
				define(Bit.ZERO) {
					"00".binaryTrieFullMatch
				}!!.binaryTriePartialMatch
			}
			.assertEqualTo(
				emptyTrie
					.set(Bit.ZERO,
						emptyTrie.set(Bit.ZERO,
							"00".binaryTrieFullMatch).binaryTriePartialMatch))
	}

	@Test
	fun defineSingleThenAlternatives() {
		emptyTrie
			.define(Bit.ZERO) {
				define(Bit.ZERO) {
					"00".binaryTrieFullMatch
				}!!.binaryTriePartialMatch
			}!!
			.define(Bit.ZERO) {
				define(Bit.ONE) {
					"01".binaryTrieFullMatch
				}!!.binaryTriePartialMatch
			}
			.assertEqualTo(
				emptyTrie
					.set(Bit.ZERO,
						emptyTrie
							.set(Bit.ZERO, "00".binaryTrieFullMatch)
							.set(Bit.ONE, "01".binaryTrieFullMatch)
							.binaryTriePartialMatch))
	}

	@Test
	fun defineBitStream() {
		emptyTrie
			.defineBit(stream(Bit.ZERO, Bit.ONE)) {
				"01".binaryTrieFullMatch
			}
			.assertEqualTo(
				emptyTrie
					.set(Bit.ZERO,
						emptyTrie
							.set(Bit.ONE, "01".binaryTrieFullMatch)
							.binaryTriePartialMatch))
	}

	@Test
	fun defineConflict() {
		emptyTrie
			.define(Bit.ZERO) { "0".binaryTrieFullMatch }!!
			.define(Bit.ZERO) { "00".binaryTrieFullMatch }
			.assertEqualTo(null)
	}
}