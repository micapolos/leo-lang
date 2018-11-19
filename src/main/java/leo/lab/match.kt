package leo.lab

import leo.Rule
import leo.base.BinaryTrie
import leo.base.Bit
import leo.base.get

data class Match(
	val ruleBinaryTrieMatch: BinaryTrie.Match<Rule>)

val BinaryTrie.Match<Rule>.match
	get() =
		Match(this)

fun Match.get(bit: Bit): Match? =
	ruleBinaryTrieMatch.get(bit)?.match
