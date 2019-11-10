package leo14.typed

import leo14.*
import leo14.lambda.id

val Script.type: Type
	get() =
		when (this) {
			is UnitScript -> type()
			is LinkScript -> link.lhs.type.plus(link.line.typeLine)
		}

val ScriptLine.typeLine
	get() =
		when (this) {
			is LiteralScriptLine -> nativeLine
			is FieldScriptLine -> line(field.typeField)
		}

val ScriptField.typeField
	get() =
		string fieldTo rhs.type

fun <T> Script.typed() =
	id<T>() of type