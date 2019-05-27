package leo4

import leo.base.empty

data class Line(val string: String, val script: Script)

fun line(string: String, script: Script = script(empty)) = Line(string, script)