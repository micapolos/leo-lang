package leo.base

import kotlin.test.Test

class BinaryTrieTest {
	val match0 = "0".binaryTrieFullMatch
	val match100 = "100".binaryTrieFullMatch
	val match101 = "101".binaryTrieFullMatch
	val match11 = "11".binaryTrieFullMatch
	val match10 = binaryTrie(match100, match101).binaryTriePartialMatch
	val match1 = binaryTrie(match10, match11).binaryTriePartialMatch
	val testTrie = binaryTrie(match0, match1)

	@Test
	fun get() {
		testTrie.get(stream(Bit.ZERO)).assertEqualTo("0".the)
		testTrie.get(stream(Bit.ONE)).assertEqualTo(null)
		testTrie.get(stream(Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		testTrie.get(stream(Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		testTrie.get(stream(Bit.ONE, Bit.ZERO)).assertEqualTo(null)
		testTrie.get(stream(Bit.ONE, Bit.ONE)).assertEqualTo("11".the)
		testTrie.get(stream(Bit.ZERO, Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		testTrie.get(stream(Bit.ZERO, Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		testTrie.get(stream(Bit.ZERO, Bit.ONE, Bit.ZERO)).assertEqualTo(null)
		testTrie.get(stream(Bit.ZERO, Bit.ONE, Bit.ONE)).assertEqualTo(null)
		testTrie.get(stream(Bit.ONE, Bit.ZERO, Bit.ZERO)).assertEqualTo("100".the)
		testTrie.get(stream(Bit.ONE, Bit.ZERO, Bit.ONE)).assertEqualTo("101".the)
		testTrie.get(stream(Bit.ONE, Bit.ONE, Bit.ZERO)).assertEqualTo(null)
		testTrie.get(stream(Bit.ONE, Bit.ONE, Bit.ONE)).assertEqualTo(null)
	}

	@Test
	fun set() {
		val newTrie = testTrie.set(stream(Bit.ONE, Bit.ZERO), "x10")
		newTrie.get(stream(Bit.ZERO)).assertEqualTo("0".the)
		newTrie.get(stream(Bit.ONE)).assertEqualTo(null)
		newTrie.get(stream(Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		newTrie.get(stream(Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		newTrie.get(stream(Bit.ONE, Bit.ZERO)).assertEqualTo("x10".the)
		newTrie.get(stream(Bit.ONE, Bit.ONE)).assertEqualTo("11".the)
		newTrie.get(stream(Bit.ZERO, Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		newTrie.get(stream(Bit.ZERO, Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		newTrie.get(stream(Bit.ZERO, Bit.ONE, Bit.ZERO)).assertEqualTo(null)
		newTrie.get(stream(Bit.ZERO, Bit.ONE, Bit.ONE)).assertEqualTo(null)
		newTrie.get(stream(Bit.ONE, Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		newTrie.get(stream(Bit.ONE, Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		newTrie.get(stream(Bit.ONE, Bit.ONE, Bit.ZERO)).assertEqualTo(null)
		newTrie.get(stream(Bit.ONE, Bit.ONE, Bit.ONE)).assertEqualTo(null)
	}
}