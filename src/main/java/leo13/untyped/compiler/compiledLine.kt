package leo13.untyped.compiler

data class CompiledLine(val name: String, val rhs: Compiled)

infix fun String.lineTo(rhs: Compiled) = CompiledLine(this, rhs)
