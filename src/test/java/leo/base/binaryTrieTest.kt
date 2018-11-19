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
	fun get_bitStream() {
		testTrie.get(stream(Bit.ZERO)).assertEqualTo(match0)
		testTrie.get(stream(Bit.ONE)).assertEqualTo(match1)
		testTrie.get(stream(Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		testTrie.get(stream(Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		testTrie.get(stream(Bit.ONE, Bit.ZERO)).assertEqualTo(match10)
		testTrie.get(stream(Bit.ONE, Bit.ONE)).assertEqualTo(match11)
		testTrie.get(stream(Bit.ZERO, Bit.ZERO, Bit.ZERO)).assertEqualTo(null)
		testTrie.get(stream(Bit.ZERO, Bit.ZERO, Bit.ONE)).assertEqualTo(null)
		testTrie.get(stream(Bit.ZERO, Bit.ONE, Bit.ZERO)).assertEqualTo(null)
		testTrie.get(stream(Bit.ZERO, Bit.ONE, Bit.ONE)).assertEqualTo(null)
		testTrie.get(stream(Bit.ONE, Bit.ZERO, Bit.ZERO)).assertEqualTo(match100)
		testTrie.get(stream(Bit.ONE, Bit.ZERO, Bit.ONE)).assertEqualTo(match101)
		testTrie.get(stream(Bit.ONE, Bit.ONE, Bit.ZERO)).assertEqualTo(null)
		testTrie.get(stream(Bit.ONE, Bit.ONE, Bit.ONE)).assertEqualTo(null)
	}
}