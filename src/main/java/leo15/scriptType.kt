package leo15

import leo.base.notNullIf
import leo14.*

val Script.type: Type
	get() =
		when (this) {
			is UnitScript -> emptyType
			is LinkScript -> link.type
		}

val ScriptLink.type: Type
	get() =
		null
			?: typeAlternativeOrNull?.type
			?: if (lhs.isEmpty) line.type else lhs.type.plus(line.typeLine)

val ScriptLink.typeAlternativeOrNull: TypeAlternative?
	get() =
		line.fieldOrNull?.rhsOrNull(orName)?.let { rhs ->
			lhs.type alternativeTo rhs.type
		}

val ScriptLine.typeLine: TypeLine
	get() =
		when (this) {
			is LiteralScriptLine -> literal.typeLine
			is FieldScriptLine -> field.typeLine
		}

val ScriptLine.type: Type
	get() =
		when (this) {
			is LiteralScriptLine -> type(literal.typeLine)
			is FieldScriptLine -> field.type
		}

val ScriptField.type: Type
	get() =
		when (string) {
			anythingName -> notNullIf(rhs.isEmpty) { anythingType }
			nothingName -> notNullIf(rhs.isEmpty) { nothingType }
			functionName -> TODO()
			repeatingName -> rhs.type.repeating.toType
			recursiveName -> rhs.type.recursive.toType
			recurseName -> recurseType
			else -> null
		} ?: type(typeLine)

val ScriptField.typeLine: TypeLine
	get() =
		when (string) {
			textName -> notNullIf(rhs.isEmpty) { textTypeLine }
			numberName -> notNullIf(rhs.isEmpty) { numberTypeLine }
			nativeName -> notNullIf(rhs.isEmpty) { javaTypeLine }
			exactName -> exactName lineTo type(exactTypeLine)
			else -> null
		} ?: exactTypeLine

val ScriptField.exactTypeLine: TypeLine
	get() =
		string lineTo rhs.type
