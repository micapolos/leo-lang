package leo13.untyped.compiler

import leo13.script.Script
import leo13.script.script
import leo13.untyped.Pattern
import leo13.untyped.pattern

data class Compiled(val expression: Expression, val pattern: Pattern)

fun value(expression: Expression = expression(), pattern: Pattern = pattern()) =
	Compiled(expression, pattern)