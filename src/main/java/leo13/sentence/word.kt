package leo13.sentence

import leo.base.appendableString

data class Word(val string: String) {
	override fun toString() = appendableString { it.append(this) }
}

fun word(string: String) = Word(string)
fun Appendable.append(word: Word) = append(word.string)