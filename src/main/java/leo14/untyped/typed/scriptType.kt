package leo14.untyped.typed

import leo.base.notNullIf
import leo14.*
import leo14.untyped.*

val Script.type: Type
	get() =
		when (this) {
			is UnitScript -> emptyType
			is LinkScript -> link.type
		}

val ScriptLink.type: Type
	get() =
		if (lhs.isEmpty) line.type
		else lhs.type.plus(line.typeLine)

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
			nativeName -> notNullIf(rhs.isEmpty) { javaTypeLine }
			exactName -> TODO()
			else -> null
		} ?: string lineTo rhs.type
