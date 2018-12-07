package leo.lab.v2

import leo.Word
import leo.base.*
import leo.bitStream

data class Pattern(
	val beginWordToMap: BinaryTrie<Pattern>,
	val endResolutionOrNull: Resolution?)

fun pattern(vararg pairs: Pair<Word, Pattern>): Pattern =
	Pattern(binaryTrie(*pairs.map {
		it.first.patternMapBitStream to it.second
	}.toTypedArray()), null)

fun Pattern.get(word: Word): Pattern? =
	beginWordToMap.get(word.patternMapBitStream)?.theValueOrNull?.value

fun Pattern.end(resolution: Resolution): Pattern =
	copy(endResolutionOrNull = resolution)

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