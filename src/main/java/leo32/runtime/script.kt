package leo32.runtime

import leo.base.appendableString
import leo.base.fold
import leo32.base.List
import leo32.base.list
import leo32.base.seq

data class Script(
	val lineList: List<Line>) {
	override fun toString() = appendableString { it.append(this) }
}

fun script(vararg lines: Line) =
	Script(list(*lines))

fun script(name: String) =
	script(name to script())

val Script.lineSeq get() =
	lineList.seq

fun Appendable.append(script: Script): Appendable =
	fold(script.lineSeq) { append(it) }
