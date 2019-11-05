package leo14.dsl

import leo14.*

fun boolean(boolean: Boolean) = script("boolean" lineTo script(line(field("$boolean"))))
fun native(int: Int) = script(native(line(number(int))))
fun native(double: Double) = script(native(line(number(double))))
fun native(string: String) = script(native(line(string)))

fun it(string: String) = script(string)
fun it(int: Int) = script(int)
fun it(double: Double) = script(double)

fun apply(vararg lines: ScriptLine) = "apply" lineTo script(*lines)
fun argument(vararg lines: ScriptLine) = "argument" lineTo script(*lines)
fun native(vararg lines: ScriptLine) = "native" lineTo script(*lines)
fun function(vararg lines: ScriptLine) = "function" lineTo script(*lines)
fun text(vararg lines: ScriptLine) = "text" lineTo script(*lines)
fun vec(vararg lines: ScriptLine) = "vec" lineTo script(*lines)

fun argument(rhs: Script) = script("argument" fieldTo rhs)
fun function(rhs: Script) = script("function" fieldTo rhs)
fun text(rhs: Script) = script("text" fieldTo rhs)

fun Script.apply(rhs: Script) = plus("apply" fieldTo rhs)
val Script.native get() = plus(line("native"))

val argument = script(line(field("argument")))
val previous = script(line(field("previous")))

