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
			is StaticType -> static.typeScript
			is LinkType -> script(link.scriptLink)
			is RecursiveType -> recursive.script
			RecurseType -> script(recurseName)
		}

val ScriptStatic.typeScript: Script
	get() =
		when (script) {
			is UnitScript -> script
			is LinkScript -> script(staticName scriptLineTo script)
		}

val TypeRecursive.script: Script
	get() =
		script(recursiveName scriptLineTo type.script)

val TypeLink.scriptLink: ScriptLink
	get() =
		lhs.script scriptLinkTo line.scriptLine

val Choice.scriptLine: ScriptLine
	get() =
		eitherName scriptLineTo script

val Choice.script: Script
	get() =
		when (this) {
			EmptyChoice -> script()
			is LinkChoice -> link.script
		}

val ChoiceLink.script: Script
	get() =
		script(scriptLink)

val ChoiceLink.scriptLink: ScriptLink
	get() =
		lhs.script scriptLinkTo line.scriptLine

val TypeLine.scriptLine: ScriptLine
	get() =
		when (this) {
			is FieldTypeLine -> line(field.scriptField)
			is ChoiceTypeLine -> eitherName scriptLineTo choice.script
			NativeTypeLine -> line(nativeName)
			NumberTypeLine -> line(numberName)
			TextTypeLine -> line(textName)
			is EnumTypeLine -> enum.scriptLine
		}

val TypeField.scriptField: ScriptField
	get() =
		name scriptFieldTo rhs.script

val Enum.scriptLine: ScriptLine
	get() =
		eitherName scriptLineTo script

val Enum.script: Script
	get() =
		when (this) {
			EmptyEnum -> script()
			is LinkEnum -> script(link.scriptLink)
		}

val EnumLink.scriptLink: ScriptLink
	get() =
		lhs.script scriptLinkTo scriptLine