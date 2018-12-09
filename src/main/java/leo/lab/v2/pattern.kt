package leo.lab.v2

import leo.Word
import leo.base.*
import leo.bitStream

sealed class Pattern

data class SwitchPattern(
	val wordToPatternBinaryTrie: BinaryTrie<Pattern>,
	val endMatchOrNull: Match?,
	val caseStackOrNull: Stack<Case>?) : Pattern() {
	override fun toString() = "cases($caseStackOrNull)"
}

data class RecursionPattern(
	val recursion: Recursion) : Pattern()

val emptySwitchPattern: SwitchPattern =
	SwitchPattern(emptyBinaryTrie(), null, null)

fun SwitchPattern.plus(case: Case): SwitchPattern =
	when (case) {
		is WordCase -> plus(case)
		is EndCase -> plus(case)
	}.copy(caseStackOrNull = caseStackOrNull.push(case))

fun SwitchPattern.plus(case: WordCase): SwitchPattern =
	copy(wordToPatternBinaryTrie = wordToPatternBinaryTrie.set(case.word.caseBitStream, case.pattern))

fun SwitchPattern.plus(case: EndCase): SwitchPattern =
	copy(endMatchOrNull = case.match)

fun pattern(vararg cases: Case): SwitchPattern =
	emptySwitchPattern.fold(cases) { plus(it) }

fun pattern(recursion: Recursion): Pattern =
	RecursionPattern(recursion)

val Pattern.switchPatternOrNull: SwitchPattern?
	get() =
		this as? SwitchPattern

val Pattern.recursionPatternOrNull: RecursionPattern?
	get() =
		this as? RecursionPattern

fun Pattern.get(word: Word): Pattern? =
	switchPatternOrNull?.get(word)

fun SwitchPattern.get(word: Word): Pattern? =
	wordToPatternBinaryTrie.get(word.caseBitStream)?.theValueOrNull?.value

val Pattern.end: Match?
	get() =
		switchPatternOrNull?.endMatchOrNull

val Pattern.endPatternOrNull: Pattern?
	get() =
		end?.patternOrNull

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
