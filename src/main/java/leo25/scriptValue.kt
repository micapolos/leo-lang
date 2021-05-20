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
			is LiteralScriptLine -> literal.line
		}

val ScriptField.field: Field
	get() =
		string fieldTo rhs.value

val Literal.line: Line
	get() =
		when (this) {
			is NumberLiteral -> numberName lineTo value(line(native(number)))
			is StringLiteral -> textName lineTo value(line(native(string)))
		}