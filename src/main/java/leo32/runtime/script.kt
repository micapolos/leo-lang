package leo32.runtime

import leo.base.appendableString
import leo.base.fold
import leo32.base.*
import leo32.base.List

data class Script(
	val lineList: List<Line>) {
	override fun toString() = appendableString { it.append(this) }
}

val List<Line>.script
	get() =
		Script(this)

fun Script.plus(line: Line) =
	lineList.add(line).script

fun script(vararg lines: Line) =
	Script(list(*lines))

fun script(name: String) =
	script(name to script())

val Script.lineSeq get() =
	lineList.seq

fun Appendable.append(script: Script): Appendable =
	fold(script.lineSeq) { append(it) }

val Script.isEmpty
	get() =
		lineList.isEmpty