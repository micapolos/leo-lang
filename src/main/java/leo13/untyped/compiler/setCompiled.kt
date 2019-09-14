package leo13.untyped.compiler

data class CompiledSet(val name: String, val rhs: ExpressionCompiled)

infix fun String.setTo(rhs: ExpressionCompiled) = CompiledSet(this, rhs)
