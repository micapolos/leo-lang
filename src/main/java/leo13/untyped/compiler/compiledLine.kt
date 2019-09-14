package leo13.untyped.compiler

import leo13.untyped.expression.lineTo
import leo13.untyped.expression.op
import leo13.untyped.expression.plus
import leo13.untyped.pattern.lineTo

data class CompiledLine(val name: String, val rhs: ExpressionCompiled)

infix fun String.lineTo(rhs: ExpressionCompiled) = CompiledLine(this, rhs)

val CompiledLine.op get() = plus(name lineTo rhs.expression).op
val CompiledLine.expressionLine get() = name lineTo rhs.expression
val CompiledLine.patternLine get() = name lineTo rhs.pattern
