package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.plusName

data class Plus(val line: ExpressionLine)

fun plus(line: ExpressionLine) = Plus(line)

val Plus.scriptLine
	get() =
		plusName lineTo script(line.bodyScriptLine)