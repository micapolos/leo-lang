package leo5.script

import leo.base.failIfOr

data class Line(val string: String, val script: Script)

infix fun String.lineTo(script: Script) = Line(this, script)

fun <T> Line.match(string: String, fn: (Script) -> T) =
	failIfOr(this.string != string) { fn(script) }