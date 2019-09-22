package leo13

import leo.base.fold
import leo13.script.ScriptLine
import leo13.script.lineTo

sealed class Text : ObjectScripting() {
	override fun toString() = scriptingLine.toString()

	override val scriptingLine: ScriptLine
		get() = textName lineTo when (this) {
			is EmptyText -> empty.scriptingLine
			is LinkText -> link.scriptingLine
		}.rhs

	fun plus(character: Character) = text(linkTo(character))
}

data class EmptyText(val empty: Empty) : Text() {
	override fun toString() = super.toString()
}

data class LinkText(val link: TextLink) : Text() {
	override fun toString() = super.toString()
}

fun text(empty: Empty): Text = EmptyText(empty)
fun text(link: TextLink): Text = LinkText(link)

fun text() = text(empty)
fun text(string: String) = text(empty).fold(string.characterSeq) { plus(it) }
