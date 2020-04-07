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
			is EmptyType -> script()
			is LinkType -> script(link.scriptLink)
			is AlternativeType -> alternative.script
			is FunctionType -> function.script
			is RecursiveType -> recursive.typeScript
			RecurseType -> script(recurseName)
		}

val TypeFunction.script: Script
	get() =
		script(functionName scriptLineTo from.script.plus(doingName scriptLineTo to.script))

val TypeRecursive.typeScript: Script
	get() =
		script(recursiveName scriptLineTo type.script)

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
		if (name.typeFieldNameIsDynamic) staticName scriptFieldTo script(staticScriptField)
		else staticScriptField

val TypeField.staticScriptField: ScriptField
	get() =
		name scriptFieldTo rhs.script

val String.typeFieldNameIsDynamic: Boolean
	get() =
		when (this) {
			textName -> true
			numberName -> true
			nativeName -> true
			orName -> true
			else -> false
		}
