package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.setName
import leo13.untyped.value.ValueLine
import leo13.untyped.value.bodyScriptLine

data class Set(val line: ValueLine)

fun set(line: ValueLine) = Set(line)

val Set.scriptLine
	get() =
		setName lineTo script(line.bodyScriptLine)