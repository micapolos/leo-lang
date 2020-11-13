package leo21.dsl

import leo14.ScriptLine
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script

typealias Line = ScriptLine

fun line(name: String, vararg lines: Line) = name lineTo script(*lines)
fun number(int: Int) = line(literal(int))
fun text(string: String) = line(literal(string))
