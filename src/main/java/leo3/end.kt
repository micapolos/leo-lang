package leo3

import leo.base.appendableString
import leo.base.byte
import leo.binary.bitSeq

object End {
	override fun toString() = appendableString { it.append(this) }
}

val end = End
val End.bitSeq get() = byte(0).bitSeq
fun Appendable.append(end: End): Appendable = append(')')
