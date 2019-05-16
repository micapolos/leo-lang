package leo3

import leo.base.*
import leo.binary.utf8ByteSeq
import leo.binary.utf8String

data class Word(
	val nonZeroByteStack: Stack<Byte>) {
	override fun toString() = appendableString { it.append(this) }
}

fun word(nonZeroByteStack: Stack<Byte>) =
	Word(nonZeroByteStack)

fun wordOrNull(string: String) =
	nullOf<Stack<Byte>>()
		.fold(string.utf8ByteSeq) { push(it) }
		?.let { word(it) }

fun word(string: String) =
	wordOrNull(string)!!

fun Appendable.append(word: Word): Appendable =
	append(word.nonZeroByteStack.reverse.seq.utf8String)

val Word.byteSeq
	get() = nonZeroByteStack.reverse.seq

val defineWord = word("define")
val quoteWord = word("quote")
val unquoteWord = word("unquote")
