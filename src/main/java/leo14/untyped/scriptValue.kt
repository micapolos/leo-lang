package leo14.untyped

import leo14.*

val Script.value: Value
	get() =
		when (this) {
			is UnitScript -> value()
			is LinkScript -> value(link.sequence)
		}

val ScriptLink.sequence
	get() =
		lhs.value sequenceTo line.valueLine

val ScriptLine.valueLine
	get() =
		when (this) {
			is LiteralScriptLine -> line(literal)
			is FieldScriptLine -> line(field.valueField)
		}

val ScriptField.valueField
	get() =
		string fieldTo rhs.value

fun scriptLine(literal: Literal): ScriptLine = leo14.line(literal)
