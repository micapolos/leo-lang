package leo13.base

import leo.base.fold
import leo.binary.utf8ByteSeq
import leo.binary.utf8String
import leo13.*
import leo13.script.*
import leo13.script.Writer

data class Text(val utf8ByteStack: Stack<Byte>) {
	override fun toString() = textWriter.string(this)
}

fun text(utf8ByteStack: Stack<Byte>) = Text(utf8ByteStack)

fun text(): Text = Text(stack())

fun Text.plusUtf8(byte: Byte): Text =
	Text(utf8ByteStack.push(byte))

fun text(string: String): Text =
	text().plus(string)

fun Text.plus(char: Char): Text =
	fold(char.toString().utf8ByteSeq) { plusUtf8(it) }

fun Text.plus(string: String): Text =
	fold(string.utf8ByteSeq) { plusUtf8(it) }

val Text.string: String
	get() =
		utf8ByteStack.reverse.seq.utf8String

val textName = "text"

val textReader: Reader<Text> =
	reader(textName) {
		text(reader("utf", reader("eight", stackReader(byteReader))).unsafeValue(this))
	}

val textWriter: Writer<Text> =
	writer(textName) {
		writer("utf", writer("eight", stackWriter(byteWriter))).script(utf8ByteStack)
	}

