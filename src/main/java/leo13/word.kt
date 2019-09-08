package leo13

import leo.base.appendableString
import leo.base.fold
import leo.base.nullOf
import leo13.word.Link
import leo13.word.append

sealed class Word {
	override fun toString() = appendableString { it.append(this) }
}

data class LetterWord(val letter: Letter) : Word() {
	override fun toString() = super.toString()
}

data class LinkWord(val link: Link) : Word() {
	override fun toString() = super.toString()
}

fun word(letter: Letter): Word = LetterWord(letter)
fun word(link: Link): Word = LinkWord(link)

fun Word.plus(letter: Letter): Word =
	word(leo13.word.link(this, letter))

fun Word?.orNullPlus(letter: Letter): Word =
	this?.plus(letter) ?: word(letter)

fun word(string: String): Word =
	nullOf<Word>()
		.fold(string) { orNullPlus(letter(it)) }
		?: fail("empty")

val Word.string: String get() = toString()

fun Appendable.append(word: Word) =
	when (word) {
		is LetterWord -> append(word.letter)
		is LinkWord -> append(word.link)
	}
