package leo19.type

import leo13.array
import leo13.map
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.plus
import leo14.script

val Type.reflectScript: Script
	get() =
		when (this) {
			is StructType -> struct.reflectScript
			is ChoiceType -> choice.reflectScript
			is ArrowType -> arrow.reflectScript
		}

val Struct.reflectScript
	get() =
		script(*fieldStack.map { reflectScriptLine }.array)

val Choice.reflectScript
	get() =
		script("choice" lineTo script(*caseStack.map { reflectScriptLine }.array))

val Arrow.reflectScript
	get() =
		lhs.reflectScript.plus("giving" lineTo rhs.reflectScript)

val Field.reflectScriptLine: ScriptLine
	get() =
		name lineTo type.reflectScript

val Case.reflectScriptLine: ScriptLine
	get() =
		name lineTo type.reflectScript
