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
import leo21.token.strings.type
import leo21.token.strings.typeKeyword


val Type.printScript: Script
	get() =
		when (this) {
			is StructType -> struct.printScript
			is ChoiceType -> choice.printScript
			is RecursiveType -> recursive.printScript
			is RecurseType -> recurse.printScript
		}

val Struct.printScript: Script
	get() =
		lineStack.printScript

val Choice.printScript: Script
	get() =
		script("choice".typeKeyword lineTo lineStack.printScript)

val Stack<Line>.printScript
	get() =
		script(*map { printScriptLine }.array)

val Line.printScriptLine: ScriptLine
	get() =
		when (this) {
			StringLine -> "text".typeKeyword.scriptLine
			NumberLine -> "number".typeKeyword.scriptLine
			is FieldLine -> field.printScriptLine
			is ArrowLine -> arrow.printScriptLine
		}

val Field.printScriptLine: ScriptLine
	get() =
		name.type lineTo rhs.printScript

val Arrow.printScriptLine: ScriptLine
	get() =
		"function".typeKeyword lineTo printScript

val Arrow.printScript: Script
	get() =
		lhs.printScript.plus("does".typeKeyword lineTo rhs.printScript)

val Recursive.printScript: Script
	get() =
		script("recursive".typeKeyword lineTo type.printScript)

val Recurse.printScript: Script
	get() =
		script("recurse".typeKeyword lineTo script(literal(index)))