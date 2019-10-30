package leo13.js

import leo.base.appendableString
import leo.base.fold
import leo13.*

interface Writer {
	fun write(char: Char): Writer

	fun write(string: String): Writer =
		fold(string, Writer::write)

	fun writeLine(string: String): Writer =
		write(string).write('\n')

	fun writeLines(vararg strings: String): Writer =
		fold(strings) { writeLine(it) }

	fun writeValueOpening() =
		writeLines(
			"function() {",
			"var $valueLiteral = null")

	fun writeValueClosing() =
		writeLines(
			"return $valueLiteral",
			"}()")

	fun writeLiteral(double: Double) =
		write("$double")

	fun writeLiteral(string: String) =
		write("\'$string\'")

	val valueLiteral get() = "__value"

	fun writeValueSet(fn: Writer.() -> Writer) =
		write("$valueLiteral = ").fn().write('\n')

	fun writeValueUpdate(operator: Char) =
		write("$valueLiteral $operator= ")
}

data class StackWriter(val stack: Stack<Char>) : Writer {
	override fun write(char: Char) = StackWriter(stack.push(char))
}

fun writeString(fn: Writer.() -> Writer): String =
	appendableString {
		it.fold((fn(StackWriter(stack())) as StackWriter).stack.reverse) { char ->
			append(char)
		}
	}
