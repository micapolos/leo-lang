package leo.lab.v2

import leo.Word
import leo.base.*
import leo.bitStream

data class Pattern(
	val beginWordToMap: BinaryTrie<Pattern>,
	val endResolutionOrNull: Resolution?)

fun patternMap(vararg pairs: Pair<Word, Pattern>): BinaryTrie<Pattern> =
	binaryTrie(*pairs.map {
		it.first.patternMapBitStream to it.second
	}.toTypedArray())

fun pattern(beginWordToMap: BinaryTrie<Pattern>, endResolutionOrNull: Resolution? = null): Pattern =
	Pattern(beginWordToMap, endResolutionOrNull)

fun pattern(endResolutionOrNull: Resolution? = null): Pattern =
	Pattern(patternMap(), endResolutionOrNull)

fun Pattern.get(word: Word): Pattern? =
	beginWordToMap.get(word.patternMapBitStream)?.theValueOrNull?.value

fun Pattern.invoke(script: Script): Script =
	match(this)
		.resolver
		.invoke(script)
		?.match
		?.templateOrNull
		?.script
		?: script

val Word.patternMapBitStream: Stream<Bit>
	get() =
		bitStream.then { Bit.ONE.onlyStream }