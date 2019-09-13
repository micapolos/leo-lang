package leo13.untyped.compiler

data class CompiledCase(val name: String, val rhs: Compiled)

infix fun String.caseTo(rhs: Compiled) = CompiledCase(this, rhs)