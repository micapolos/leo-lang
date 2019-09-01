package leo13.value

import leo.base.SeqNode
import leo.base.then
import leo13.LeoObject
import leo13.script.script

data class ValueLink(val lhs: Value, val line: ValueLine) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "link"
	override val scriptableBody get() = script(lhs.scriptableLine, line.scriptableLine)
}

fun link(lhs: Value, line: ValueLine) = ValueLink(lhs, line)

val ValueLink.lineOrNullSeqNode: SeqNode<ValueLine?> get() = line then lhs.lineOrNullSeq
