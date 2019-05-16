package leo3

import leo.base.appendableString
import leo.base.byte
import leo.base.onlySeq
import leo.binary.zero

object End {
	override fun toString() = appendableString { it.append(this) }
}

val end = End

val End.byteSeq get() = zero.byte.onlySeq

fun Appendable.append(end: End): Appendable =
	append(')')