package leo13.untyped.expression

import leo13.plusName
import leo13.script.lineTo
import leo13.script.script

data class Plus(val line: ExpressionLine)

fun plus(line: ExpressionLine) = Plus(line)
fun plus(name: String) = plus(name lineTo expression())

val Plus.scriptLine
	get() =
		plusName lineTo script(line.bodyScriptLine)