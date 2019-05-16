package leo3

import leo.base.appendableString
import leo.base.byte
import leo.base.flatSeq
import leo.base.onlySeq
import leo.binary.zero

data class Begin(
	val word: Word) {
	override fun toString() = appendableString { it.append(this) }
}

fun begin(word: Word) = Begin(word)

val Begin.byteSeq
	get() = flatSeq(word.byteSeq, zero.byte.onlySeq)

fun Appendable.append(begin: Begin): Appendable =
	append(begin.word).append('(')
