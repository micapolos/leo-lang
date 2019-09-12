package leo13.untyped.compiler

import leo13.script.lineTo
import leo13.untyped.lineTo

data class CompiledLine(val name: String, val rhs: Compiled)

infix fun String.lineTo(rhs: Compiled) = CompiledLine(this, rhs)

val CompiledLine.scriptLine get() = name lineTo rhs.script
val CompiledLine.patternLine get() = name lineTo rhs.pattern