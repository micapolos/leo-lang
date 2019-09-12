package leo13.untyped.compiler

import leo13.untyped.Pattern
import leo13.untyped.expression.Expression
import leo13.untyped.expression.plus
import leo13.untyped.plus

data class Match(val expression: Expression, val pattern: Pattern)

infix fun Expression.match(pattern: Pattern) = Match(this, pattern)

fun Match.plus(line: MatchLine) =
	expression.plus(line.op).match(pattern.plus(line.patternLine))
