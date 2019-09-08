package leo13.word

import leo.base.appendableString
import leo13.Letter
import leo13.Word
import leo13.append
import leo13.word

data class Link(val word: Word, val letter: Letter) {
	override fun toString() = appendableString { it.append(this) }
}

fun link(word: Word, letter: Letter) = Link(word, letter)
infix fun Link.plus(letter: Letter) = Link(word(this), letter)
fun Appendable.append(link: Link): Appendable = append(link.word).append(link.letter)
