package leo14.untyped.typed

import leo14.*
import leo14.line
import leo14.untyped.*
import leo14.fieldTo as scriptFieldTo
import leo14.lineTo as scriptLineTo
import leo14.linkTo as scriptLinkTo

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
		script(functionName scriptLineTo from.script.plus(doingName scriptLineTo to.script))

val TypeRecursive.script: Script
	get() =
		script(recursiveName scriptLineTo type.script)

val TypeRepeating.script: Script
	get() =
		script(repeatingName scriptLineTo type.script)

val TypeLink.scriptLink: ScriptLink
	get() =
		lhs.script scriptLinkTo line.scriptLine

val TypeLine.scriptLine: ScriptLine
	get() =
		when (this) {
			is LiteralTypeLine -> line(literal)
			is FieldTypeLine -> line(field.scriptField)
			NativeTypeLine -> line(nativeName)
			NumberTypeLine -> line(numberName)
			TextTypeLine -> line(textName)
		}

val TypeAlternative.script: Script
	get() =
		lhs.script.plus(orName scriptLineTo rhs.script)

val TypeField.scriptField: ScriptField
	get() =
		if (name.isTypeKeyword) exactName scriptFieldTo script(exactScriptField)
		else exactScriptField

val TypeField.exactScriptField: ScriptField
	get() =
		name scriptFieldTo rhs.script

val String.isTypeKeyword: Boolean
	get() =
		when (this) {
			textName -> true
			numberName -> true
			nativeName -> true
			orName -> true
			recursiveName -> true
			recurseName -> true
			exactName -> true
			anythingName -> true
			nothingName -> true
			repeatingName -> true
			else -> false
		}
