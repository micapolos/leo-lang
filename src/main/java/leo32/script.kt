package leo32

import leo.base.string
import leo32.base.*
import leo32.base.List

data class Script(val lineList: List<Line>) {
	override fun toString() = code.string
}

val List<Line>.script
	get() =
		Script(this)

fun Script.plus(line: Line) =
	lineList.add(line).script

fun script(vararg lines: Line) =
	Script(list(*lines))

fun script(symbol: Symbol) =
	script(symbol to script())

val Script.lineSeq get() =
	lineList.seq

val Script.isEmpty
	get() =
		lineList.isEmpty