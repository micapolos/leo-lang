package leo

import leo.base.*
import leo.binary.Bit

data class Match(
	val bodyBinaryTrieMatch: BinaryTrie.Match<Body>)

val BinaryTrie.Match<Body>.match
	get() =
		Match(this)

val Body.match: Match
	get() =
		binaryTrieFullMatch.match

fun Match.get(bit: Bit): Match? =
	bodyBinaryTrieMatch.get(bit)?.match

val Function.match: Match
	get() =
		bodyBinaryTrie.binaryTriePartialMatch.match

val Match.functionOrNull: Function?
	get() =
		(bodyBinaryTrieMatch as? BinaryTrie.Match.Partial<Body>)?.binaryTrie?.function

val Match.bodyOrNull: Body?
	get() =
		bodyBinaryTrieMatch.theValueOrNull?.value