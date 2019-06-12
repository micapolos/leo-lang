package leo5.script

data class Line(val string: String, val script: Script)

infix fun String.lineTo(script: Script) = Line(this, script)

fun Appendable.append(line: Line) = append(line.string).append('(').append(line.script).append(')')
