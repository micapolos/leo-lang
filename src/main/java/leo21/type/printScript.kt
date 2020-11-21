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
		lineStack.printScript

val Choice.printScriptLine: ScriptLine
	get() =
		"choice".typeKeyword lineTo lineStack.printScript

val Stack<Line>.printScript
	get() =
		script(*map { printScriptLine }.array)

val Line.printScriptLine: ScriptLine
	get() =
		when (this) {
			StringLine -> "text".typeKeyword.scriptLine
			NumberLine -> "number".typeKeyword.scriptLine
			is FieldLine -> field.printScriptLine
			is ChoiceLine -> choice.printScriptLine
			is ArrowLine -> arrow.printScriptLine
			is RecursiveLine -> recursive.printScriptLine
			is RecurseLine -> recurse.printScriptLine
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

val Recursive.printScriptLine: ScriptLine
	get() =
		"recursive".typeKeyword lineTo script(line.printScriptLine)

val Recurse.printScriptLine: ScriptLine
	get() =
		"recurse".typeKeyword lineTo script(literal(index))