package leo15

import leo14.*
import leo14.untyped.*
import leo14.untyped.typed.*

val Type.reflectScriptLine: ScriptLine
	get() =
		"type" lineTo script

val Type.script: Script
	get() =
		when (this) {
			EmptyType -> script()
			is LinkType -> script(link.scriptLink)
			is AlternativeType -> alternative.script
			is FunctionType -> function.script
			is RepeatingType -> repeating.script
			is RecursiveType -> recursive.script
			RecurseType -> script(recurseName)
			AnythingType -> script(anythingName)
			NothingType -> script(nothingName)
		}

val TypeFunction.script: Script
	get() =
		script(functionName lineTo from.script.plus(doingName lineTo to.script))

val TypeRecursive.script: Script
	get() =
		script(recursiveName lineTo type.script)

val TypeRepeating.script: Script
	get() =
		script(repeatingName lineTo type.script)

val TypeLink.scriptLink: ScriptLink
	get() =
		lhs.script linkTo line.scriptLine

val TypeLine.scriptLine: ScriptLine
	get() =
		when (this) {
			textTypeLine -> leo14.line(textName)
			numberTypeLine -> leo14.line(numberName)
			is LiteralTypeLine -> leo14.line(literal)
			is FieldTypeLine -> leo14.line(field.scriptField)
			JavaTypeLine -> leo14.line(javaName)
		}

val TypeAlternative.script: Script
	get() =
		lhs.script.plus(orName lineTo rhs.script)

val TypeField.scriptField: ScriptField
	get() =
		if (name.isTypeKeyword) exactName fieldTo script(exactScriptField)
		else exactScriptField

val TypeField.exactScriptField: ScriptField
	get() =
		name fieldTo rhs.script

val String.isTypeKeyword: Boolean
	get() =
		when (this) {
			javaName -> true
			orName -> true
			recursiveName -> true
			recurseName -> true
			exactName -> true
			anythingName -> true
			nothingName -> true
			repeatingName -> true
			else -> false
		}
