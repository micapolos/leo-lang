package leo13.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.setName

data class Set(val line: ExpressionLine)

fun set(line: ExpressionLine) = Set(line)

val Set.scriptLine
	get() =
		setName lineTo script(line.bodyScriptLine)