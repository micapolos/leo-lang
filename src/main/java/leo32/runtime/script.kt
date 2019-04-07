package leo32.runtime

import leo32.base.List
import leo32.base.list
import leo32.base.seq

data class Script(
	val lineList: List<Line>)

fun script(vararg lines: Line) =
	Script(list(*lines))

val Script.lineSeq get() =
	lineList.seq