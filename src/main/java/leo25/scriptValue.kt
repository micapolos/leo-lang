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
		lhs.value.plus(line.field)

val ScriptLine.field: Field
	get() =
		when (this) {
			is FieldScriptLine -> field.field
			is LiteralScriptLine -> literal.field
		}

val ScriptField.field: Field
	get() =
		string fieldTo rhs.value

val Literal.field: Field
	get() =
		when (this) {
			is NumberLiteral -> numberName fieldTo rhs(native(number))
			is StringLiteral -> textName fieldTo rhs(native(string))
		}