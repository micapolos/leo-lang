package leo14.typed

import leo14.*
import leo14.lambda.id

val Script.staticType: Type
	get() =
		when (this) {
			is UnitScript -> type()
			is LinkScript -> link.lhs.staticType.plus(link.line.staticTypeLine)
		}

val ScriptLine.staticTypeLine
	get() =
		when (this) {
			is LiteralScriptLine -> nativeLine
			is FieldScriptLine -> line(field.staticTypeField)
		}

val ScriptField.staticTypeField
	get() =
		string fieldTo rhs.staticType

fun <T> Script.staticTyped() =
	id<T>() of staticType