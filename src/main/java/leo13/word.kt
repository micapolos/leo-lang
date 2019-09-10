package leo13

import leo.base.appendableString
import leo.base.fold
import leo.base.nullOf
import leo13.container.*
import leo13.container.List

data class Word(val letterList: List<Letter>) {
	override fun toString() = appendableString { it.append(this) }
}

fun word(letterList: List<Letter>): Word = Word(letterList)
fun word(letter: Letter): Word = word(list(letter))

fun Word.plus(letter: Letter): Word =
	word(letterList.plus(letter))

fun word(string: String): Word =
	nullOf<Word>()
		.fold(string) { this?.plus(letter(it)) ?: word(letter(it)) }
		?: fail("empty")

val Word.string: String get() = toString()

fun Appendable.append(word: Word) =
	fold(word.letterList.reverse) { append(it) }

fun Word.failableUnit(word: Word): Failable<Unit> =
	if (this != word) failure(sentence(this).plus(expectedWord lineTo sentence(word)))
	else success(Unit)
