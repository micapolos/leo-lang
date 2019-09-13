package leo13.untyped.compiler

data class CompiledSet(val name: String, val rhs: Compiled)

infix fun String.setTo(rhs: Compiled) = CompiledSet(this, rhs)
