package leo21.compiled

import leo21.type.Line

sealed class Option
data class YesOption(val lineCompiled: LineCompiled) : Option()
data class NoOption(val line: Line) : Option()

fun option(lineCompiled: LineCompiled): Option = YesOption(lineCompiled)
fun option(line: Line): Option = NoOption(line)
