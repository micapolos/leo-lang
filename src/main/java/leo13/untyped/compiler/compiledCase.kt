package leo13.untyped.compiler

data class CompiledCase(val name: String, val rhs: ExpressionCompiled)

infix fun String.caseTo(rhs: ExpressionCompiled) = CompiledCase(this, rhs)