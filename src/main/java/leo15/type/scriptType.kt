package leo15.type

import leo13.choiceName
import leo14.*
import leo15.*

val Script.type: Type
	get() =
		when (this) {
			is UnitScript -> emptyType
			is LinkScript -> link.type
		}

val ScriptLink.type: Type
	get() =
		if (lhs.isEmpty) line.type
		else lhs.type.plus(line.choice)

val ScriptLine.type: Type
	get() =
		when (this) {
			is LiteralScriptLine -> literal.typeLine.choice.type
			is FieldScriptLine -> field.type
		}

val ScriptLine.choice: Choice
	get() =
		when (this) {
			is LiteralScriptLine -> literal.typeLine.choice
			is FieldScriptLine -> field.choice
		}

val ScriptField.type: Type
	get() =
		when (string) {
			repeatingName -> rhs.linkOrNull?.onlyLineOrNull?.fieldOrNull?.choice?.repeating?.toType
			recursiveName -> rhs.type.recursive.toType
			recurseName -> recurseType
			else -> null
		} ?: choice.type

val ScriptField.choice: Choice
	get() =
		if (string == choiceName) rhs.choice
		else typeLine.choice

val Script.choice: Choice
	get() =
		when (this) {
			is UnitScript -> emptyChoice
			is LinkScript -> link.choice
		}

val ScriptLink.choice: Choice
	get() =
		lhs.choice.plus(line.typeLine)

val ScriptLine.typeLine: TypeLine
	get() =
		when (this) {
			is LiteralScriptLine -> literal.typeLine
			is FieldScriptLine -> field.typeLine
		}

val ScriptField.typeLine: TypeLine
	get() =
		when (string) {
			numberName -> numberTypeLine
			textName -> textTypeLine
			javaName -> javaTypeLine
			else -> string lineTo rhs.type
		}

