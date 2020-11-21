package leo21.type

import leo13.Stack
import leo13.array
import leo13.map
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.literal
import leo14.plus
import leo14.script
import leo14.scriptLine

val Type.script: Script
	get() =
		lineStack.script

val Choice.scriptLine: ScriptLine
	get() =
		"choice" lineTo lineStack.script

val Stack<Line>.script
	get() =
		script(*map { scriptLine }.array)

val Line.scriptLine: ScriptLine
	get() =
		when (this) {
			StringLine -> "text".scriptLine
			NumberLine -> "number".scriptLine
			is FieldLine -> field.scriptLine
			is ChoiceLine -> choice.scriptLine
			is ArrowLine -> arrow.scriptLine
			is RecursiveLine -> recursive.scriptLine
			is RecurseLine -> recurse.scriptLine
		}

val Field.scriptLine: ScriptLine
	get() =
		name lineTo rhs.script

val Arrow.scriptLine: ScriptLine
	get() =
		"function" lineTo script

val Arrow.script: Script
	get() =
		lhs.script.plus("does" lineTo rhs.script)

val Recursive.scriptLine: ScriptLine
	get() =
		"recursive" lineTo script(line.scriptLine)

val Recurse.scriptLine: ScriptLine
	get() =
		"recurse" lineTo script(literal(index))