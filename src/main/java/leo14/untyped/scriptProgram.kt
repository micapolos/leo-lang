package leo14.untyped

import leo14.*

val Script.program: Program
	get() =
		when (this) {
			is UnitScript -> program()
			is LinkScript -> program(link.sequence)
		}

val ScriptLink.sequence
	get() =
		lhs.program sequenceTo line.value

val ScriptLine.value
	get() =
		when (this) {
			is LiteralScriptLine -> value(literal)
			is FieldScriptLine -> value(field.valueField)
		}

val ScriptField.valueField
	get() =
		string fieldTo rhs.program