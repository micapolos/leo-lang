package leo14.dsl

import leo14.*

fun apply(vararg lines: ScriptLine) = "apply" lineTo script(*lines)
fun argument(vararg lines: ScriptLine) = "argument" lineTo script(*lines)
fun native(vararg lines: ScriptLine) = "native" lineTo script(*lines)
fun function(vararg lines: ScriptLine) = "function" lineTo script(*lines)

val argument = line(field("argument"))
val previous = line(field("previous"))

fun native(int: Int) = native(line(number(int)))
fun native(double: Double) = native(line(number(double)))
fun native(string: String) = native(line(string))
