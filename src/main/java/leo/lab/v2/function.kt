package leo.lab.v2

import leo.Word
import leo.base.*
import leo.bitStream

sealed class Function

data class SwitchFunction(
	val wordToFunctionBinaryTrie: BinaryTrie<Function>,
	val endMatchOrNull: Match?,
	val caseStackOrNull: Stack<Case>?) : Function() {
	override fun toString() = "cases($caseStackOrNull)"
}

data class RecursionFunction(
	val recursion: Recursion) : Function()

val identityFunction: SwitchFunction =
	SwitchFunction(emptyBinaryTrie(), null, null)

fun SwitchFunction.plus(case: Case): SwitchFunction =
	when (case) {
		is WordCase -> plus(case)
		is EndCase -> plus(case)
	}.copy(caseStackOrNull = caseStackOrNull.push(case))

fun SwitchFunction.plus(case: WordCase): SwitchFunction =
	copy(wordToFunctionBinaryTrie = wordToFunctionBinaryTrie.set(case.word.caseBitStream, case.function))

fun SwitchFunction.plus(case: EndCase): SwitchFunction =
	copy(endMatchOrNull = case.match)

fun function(vararg cases: Case): SwitchFunction =
	identityFunction.fold(cases) { plus(it) }

fun function(recursion: Recursion): Function =
	RecursionFunction(recursion)

val Function.switchFunctionOrNull: SwitchFunction?
	get() =
		this as? SwitchFunction

val Function.recursionFunctionOrNull: RecursionFunction?
	get() =
		this as? RecursionFunction

fun Function.get(word: Word): Function? =
	switchFunctionOrNull?.get(word)

fun SwitchFunction.get(word: Word): Function? =
	wordToFunctionBinaryTrie.get(word.caseBitStream)?.theValueOrNull?.value

val Function.end: Match?
	get() =
		switchFunctionOrNull?.endMatchOrNull

val Function.endFunctionOrNull: Function?
	get() =
		end?.functionOrNull

fun Function.invoke(script: Script): Script =
	resolver
		.invoke(script)
		?.match
		?.bodyOrNull
		?.invoke(script)
		?: script

val Word.caseBitStream: Stream<Bit>
	get() =
		bitStream.then { Bit.ONE.onlyStream }
