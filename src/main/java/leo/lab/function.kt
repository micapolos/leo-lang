package leo.lab

import leo.*
import leo.base.*

data class Function(
	val ruleBinaryTrie: BinaryTrie<Rule>)

val BinaryTrie<Rule>.function
	get() =
		Function(this)

val emptyFunction: Function =
	emptyBinaryTrie<Rule>().function

fun Function.get(bit: Bit): Match? =
	ruleBinaryTrie.get(bit)?.match

fun Function.set(bit: Bit, matchOrNull: Match?): Function =
	ruleBinaryTrie.set(bit, matchOrNull?.ruleBinaryTrieMatch).function

// === define

fun Function.define(patternTerm: Term<Pattern>, rule: Rule): Function? =
	(this to rule.binaryTrieFullMatch.match).functionDefine(patternTerm)?.first

fun Pair<Function, Match>.functionDefine(patternTerm: Term<Pattern>): Pair<Function, Match>? =
	orNull.fold(patternTerm.reversedTokenStream) { token ->
		functionDefine(token)
	}

fun Pair<Function, Match>.functionDefine(pattern: Pattern): Pair<Function, Match>? =
	orNull.fold(pattern.patternTermStack.reverse.stream) { patternTerm ->
		this?.functionDefine(patternTerm)
	}

fun Pair<Function, Match>.functionDefine(token: Token<Pattern>): Pair<Function, Match>? =
	when (token) {
		is Token.MetaEnd -> functionDefine(token)
		is Token.Begin -> functionDefine(token)
		is Token.End -> functionDefineEndToken()
	}

fun Pair<Function, Match>.functionDefine(token: Token.MetaEnd<Pattern>): Pair<Function, Match>? =
	functionDefine(token.value)

fun Pair<Function, Match>.functionDefine(token: Token.Begin<Pattern>): Pair<Function, Match>? =
	this
		.functionDefine(token.word)
		?.functionDefine(beginByte)

fun Pair<Function, Match>.functionDefineEndToken(): Pair<Function, Match>? =
	functionDefine(endByte)

fun Pair<Function, Match>.functionDefine(word: Word): Pair<Function, Match>? =
	orNull.fold(word.letterStack) { letter ->
		functionDefine(letter)
	}

fun Pair<Function, Match>.functionDefine(letter: Letter): Pair<Function, Match>? =
	functionDefine(letter.byte)

fun Pair<Function, Match>.functionDefine(byte: Byte): Pair<Function, Match>? =
	orNull.fold(byte.bitStack) { bit ->
		functionDefine(bit)
	}

fun Pair<Function, Match>.functionDefine(bit: Bit): Pair<Function, Match>? =
	let { (function, accumulatedMatch) ->
		function.get(bit).let { currentMatchOrNull ->
			if (currentMatchOrNull == null)
				function.set(bit, accumulatedMatch) to accumulatedMatch
			else
				when (accumulatedMatch.ruleBinaryTrieMatch) {
					is BinaryTrie.Match.Full -> null
					is BinaryTrie.Match.Partial ->
						when (currentMatchOrNull.ruleBinaryTrieMatch) {
							is BinaryTrie.Match.Full -> null
							is BinaryTrie.Match.Partial -> TODO()
						}
				}
		}
	}
