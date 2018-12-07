package leo.lab.v2

import leo.Word
import leo.base.*
import leo.bitStream

sealed class Pattern

data class SwitchPattern(
	val wordToPatternBinaryTrie: BinaryTrie<Pattern>,
	val endMatchOrNull: Match?) : Pattern()

data class RecursionPattern(
	val recursion: Recursion) : Pattern()

val emptySwitchPattern: SwitchPattern =
	SwitchPattern(emptyBinaryTrie(), null)

fun SwitchPattern.plus(case: Case): SwitchPattern =
	when (case) {
		is WordCase -> plus(case)
		is EndCase -> plus(case)
	}

fun SwitchPattern.plus(case: WordCase): SwitchPattern =
	copy(wordToPatternBinaryTrie = wordToPatternBinaryTrie.set(case.word.caseBitStream, case.pattern))

fun SwitchPattern.plus(case: EndCase): SwitchPattern =
	copy(endMatchOrNull = case.match)

fun pattern(vararg cases: Case): Pattern =
	emptySwitchPattern.fold(cases) { plus(it) }

fun pattern(recursion: Recursion): Pattern =
	RecursionPattern(recursion)

fun SwitchPattern.get(word: Word): Pattern? =
	wordToPatternBinaryTrie.get(word.caseBitStream)?.theValueOrNull?.value

fun Pattern.invoke(script: Script): Script =
	resolver
		.invoke(script)
		?.match
		?.templateOrNull
		?.script
		?: script

val Word.caseBitStream: Stream<Bit>
	get() =
		bitStream.then { Bit.ONE.onlyStream }
