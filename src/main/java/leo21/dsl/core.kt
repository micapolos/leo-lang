package leo21.dsl

import leo14.ScriptLine
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.matching.Case
import leo14.matching.get
import leo14.script

typealias Line = ScriptLine

fun _line(name: String, vararg lines: Line) = name lineTo script(*lines)
fun Line._get(name: String) = get(name)
fun _case(name: String, fn: (ScriptLine) -> ScriptLine) = Case(name, fn)

fun number(int: Int) = line(literal(int))
fun text(string: String) = line(literal(string))
