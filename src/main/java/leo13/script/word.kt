package leo13.script

import leo.base.fold
import leo.base.nullOf
import leo13.ObjectScripting
import leo13.wordName

sealed class Word : ObjectScripting() {
	override fun toString() = scriptingLine.toString()

	override val scriptingLine: ScriptLine
		get() = wordName lineTo script(name)

	val name
		get() =
			when (this) {
				is LetterWord -> letter.char.toString()
				is LinkWord -> link.name
			}

	fun plus(letter: Letter) = word(linkTo(letter))
}

data class LetterWord(val letter: Letter) : Word() {
	override fun toString() = super.toString()
}

data class LinkWord(val link: WordLink) : Word() {
	override fun toString() = super.toString()
}

fun word(letter: Letter): Word = LetterWord(letter)
fun word(link: WordLink): Word = LinkWord(link)

fun word(string: String) =
	nullOf<Word>()
		.fold(string) {
			letter(it).let { letter ->
				this?.plus(letter) ?: word(letter)
			}
		}!!
