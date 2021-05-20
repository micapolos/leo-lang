package leo25

import leo14.*

val Script.value: Value
	get() =
		when (this) {
			is UnitScript -> value()
			is LinkScript -> link.value
		}

val ScriptLink.value: Value
	get() =
		lhs.value.plus(line.line)

val ScriptLine.line: Line
	get() =
		when (this) {
			is FieldScriptLine -> line(field.field)
			is LiteralScriptLine -> line(literal)
		}

val ScriptField.field: Field
	get() =
		string fieldTo rhs.value
