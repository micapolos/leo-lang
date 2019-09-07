package leo13.base

import leo.base.fold
import leo.binary.utf8ByteSeq
import leo.binary.utf8String
import leo13.scripter.Scripter
import leo13.scripter.field
import leo13.scripter.scripter
import leo13.scripter.toString
import leo9.*

val textScripter: Scripter<Text> =
	scripter(
		"text",
		field(scripter("utf", scripter("eight", stackType(byteScripter)))) { utf8ByteStack }
	) { Text(it) }

data class Text(val utf8ByteStack: Stack<Byte>) {
	override fun toString() = textScripter.toString(this)
}

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
