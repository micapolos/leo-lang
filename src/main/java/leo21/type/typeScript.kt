package leo21.type

import leo13.Stack
import leo13.array
import leo13.map
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.plus
import leo14.script

val Type.script: Script
	get() =
		when (this) {
			is StructType -> struct.script
			is ChoiceType -> choice.script
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
			StringLine -> "text" lineTo script()
			DoubleLine -> "number" lineTo script()
			is FieldLine -> field.scriptLine
			is ArrowLine -> "function" lineTo arrow.script
		}

val Field.scriptLine: ScriptLine
	get() =
		name lineTo rhs.script

val Arrow.script: Script
	get() =
		lhs.script.plus("doing" lineTo rhs.script)