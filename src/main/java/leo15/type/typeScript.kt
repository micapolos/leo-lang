package leo15.type

import leo13.choiceName
import leo14.*
import leo15.*

val Type.script: Script
	get() =
		when (this) {
			EmptyType -> script()
			is LinkType -> link.script
			is RepeatingType -> repeating.script
			is RecursiveType -> recursive.script
			RecurseType -> script(recurseName)
		}

val TypeLink.script: Script
	get() =
		lhs.script.plus(choice.scriptLine)

val Choice.scriptLine: ScriptLine
	get() =
		onlyLineOrNull
			?.run { scriptLine }
			?: choiceName lineTo script

val Choice.script: Script
	get() =
		when (this) {
			EmptyChoice -> script()
			is LinkChoice -> link.script
		}

val ChoiceLink.script: Script
	get() =
		lhs.script.plus(line.scriptLine)

val TypeLine.scriptLine: ScriptLine
	get() =
		builtinScriptLine ?: when (this) {
			is LiteralTypeLine -> line(literal)
			is FieldTypeLine -> field.scriptLine
			is ArrowTypeLine -> arrow.scriptLine
			JavaTypeLine -> line(javaName)
		}

val TypeLine.builtinScriptLine: ScriptLine?
	get() =
		when (this) {
			numberTypeLine -> line(numberName)
			textTypeLine -> line(textName)
			else -> null
		}

val TypeField.scriptLine: ScriptLine
	get() =
		name lineTo rhs.script

val Arrow.scriptLine: ScriptLine
	get() =
		functionName lineTo lhs.script.plus(givingName lineTo rhs.script)

val Repeating.script: Script
	get() =
		script(repeatingName lineTo script(choice.scriptLine))

val Recursive.script: Script
	get() =
		script(recursiveName lineTo type.script)
