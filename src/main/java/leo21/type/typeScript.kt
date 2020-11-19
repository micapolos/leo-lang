package leo21.type

import leo13.Stack
import leo13.array
import leo13.map
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.literal
import leo14.script
import leo14.scriptLine

val Type.script: Script
	get() =
		when (this) {
			is StructType -> struct.script
			is ChoiceType -> choice.script
			is RecursiveType -> recursive.script
			is RecurseType -> recurse.script
		}

val Struct.script: Script
	get() =
		lineStack.script

val Choice.script: Script
	get() =
		script("choice" lineTo lineStack.script)

val Stack<Line>.script
	get() =
		script(*map { scriptLine }.array)

val Line.scriptLine: ScriptLine
	get() =
		when (this) {
			StringLine -> "text".scriptLine
			NumberLine -> "number".scriptLine
			is FieldLine -> field.scriptLine
			is ArrowLine -> arrow.scriptLine
		}

val Field.scriptLine: ScriptLine
	get() =
		name lineTo rhs.script

val Arrow.scriptLine: ScriptLine
	get() =
		"function" lineTo script

val Arrow.script: Script
	get() =
		script(
			"it" lineTo lhs.script,
			"does" lineTo rhs.script)

val Recursive.script: Script
	get() =
		script("recursive" lineTo type.script)

val Recurse.script: Script
	get() =
		script("recurse" lineTo script(literal(index)))