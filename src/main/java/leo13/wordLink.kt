package leo13

import leo.base.appendableString

data class WordLink(val word: Word, val letter: Letter) {
	override fun toString() = appendableString { it.append(this) }
}

fun link(word: Word, letter: Letter) =
	WordLink(word, letter)

infix fun WordLink.plus(letter: Letter) =
	WordLink(word(this), letter)

fun Appendable.append(link: WordLink): Appendable =
	append(link.word).append(link.letter)
