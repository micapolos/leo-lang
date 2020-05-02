package leo16

import leo.base.appendableString
import leo13.*

sealed class Text {
	override fun toString() = string
}

data class StringText(val string: String) : Text() {
	override fun toString() = super.toString()
}

data class StackText(val textStack: Stack<Text>) : Text() {
	override fun toString() = super.toString()
}

val String.text: Text get() = StringText(this)
val Stack<Text>.text: Text get() = StackText(this)

val Text.textStack: Stack<Text>
	get() =
		when (this) {
			is StringText -> stack(this)
			is StackText -> textStack
		}

operator fun Text.plus(text: Text) =
	textStack.push(text).text

val Text.string: String
	get() =
		appendableString { it.append(this) }

fun Appendable.append(text: Text): Appendable =
	when (text) {
		is StringText -> append(text.string)
		is StackText -> fold(text.textStack.reverse) { append(it) }
	}
