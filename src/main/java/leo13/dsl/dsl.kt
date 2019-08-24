package leo13.dsl

import leo13.ScriptLine
import leo13.lineTo
import leo13.script

typealias X = ScriptLine

fun x(name: String, vararg lines: ScriptLine): ScriptLine = name lineTo script(*lines)

fun case(vararg x: X) = x("case", *x)
fun expr(vararg x: X) = x("expr", *x)
fun one(vararg x: X) = x("one", *x)
fun switch(vararg x: X) = x("switch", *x)
fun _to(vararg x: X) = x("to", *x)
fun zero(vararg x: X) = x("zero", *x)
