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
			choiceName -> rhs.choiceOrNull?.type
			else -> null
		} ?: choice.type

val ScriptField.choice: Choice
	get() =
		typeLine.choice

val Script.choiceOrNull: Choice?
	get() =
		when (this) {
			is UnitScript -> null
			is LinkScript -> link.choice
		}

val ScriptLink.choice: Choice
	get() =
		when (lhs) {
			is UnitScript -> line.typeLine.choice
			is LinkScript -> lhs.link.choice.plus(line.typeLine)
		}

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

