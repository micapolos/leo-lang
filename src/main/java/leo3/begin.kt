package leo3

import leo.base.Seq
import leo.base.appendableString
import leo.base.byte
import leo.base.seqNodeOrNull
import leo.binary.bitSeq

data class Begin(
	val word: Word) {
	override fun toString() = appendableString { it.append(this) }
}

fun begin(word: Word) = Begin(word)

val Begin.bitSeq
	get() = Seq { seqNodeOrNull(word.bitSeq, byte(0).bitSeq) }

fun Appendable.append(begin: Begin): Appendable =
	append(begin.word).append('(')
