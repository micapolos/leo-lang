package leo13.untyped.compiler

import leo13.untyped.expression.lineTo
import leo13.untyped.expression.op
import leo13.untyped.expression.plus
import leo13.untyped.pattern.lineTo

data class MatchLine(val name: String, val rhs: Match)

infix fun String.lineTo(rhs: Match) = MatchLine(this, rhs)

val MatchLine.op get() = plus(name lineTo rhs.expression).op
val MatchLine.expressionLine get() = name lineTo rhs.expression
val MatchLine.patternLine get() = name lineTo rhs.pattern
