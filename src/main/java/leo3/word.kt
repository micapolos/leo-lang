package leo3

import leo.base.*
import leo.binary.Bit
import leo.binary.byteBitSeq
import leo.binary.utf8ByteSeq
import leo.binary.utf8String

data class Word(
	val nonZeroByteStack: Stack<Byte>) {
	override fun toString() = appendableString { it.append(this) }
}

fun word(nonZeroByteStack: Stack<Byte>) =
	Word(nonZeroByteStack)

fun Word?.plus(byte: Byte): Word? =
	notNullIf(byte != 0.toByte()) {
		this?.nonZeroByteStack.push(byte).run(::word)
	}

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

val Word.bitSeq: Seq<Bit>
	get() = byteSeq.byteBitSeq

tailrec fun Reader.readWordTo(word: Word): Read<Word> {
	val read = readByte()
	val wordPlusByteOrNull = word.plus(read.value)
	return if (wordPlusByteOrNull == null) Read(word, read.reader)
	else read.reader.readWordTo(wordPlusByteOrNull)
}

val defineWord = word("define")
val quoteWord = word("quote")
val unquoteWord = word("unquote")
