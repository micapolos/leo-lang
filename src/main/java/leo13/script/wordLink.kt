package leo13.script

import leo13.ObjectScripting
import leo13.linkName

data class WordLink(val word: Word, val letter: Letter) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = linkName lineTo script(name)

	val name: String get() = word.name + letter.char
}

fun Word.linkTo(letter: Letter) = WordLink(this, letter)