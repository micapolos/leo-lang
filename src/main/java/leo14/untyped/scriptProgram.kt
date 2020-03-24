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
		lhs.program sequenceTo line.programLine

val ScriptLine.programLine
	get() =
		when (this) {
			is LiteralScriptLine -> line(literal)
			is FieldScriptLine -> line(field.programField)
		}

val ScriptField.programField
	get() =
		string fieldTo rhs.program