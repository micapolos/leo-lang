package leo14.untyped

import leo14.*

val Script.sayString: String
	get() =
		when (this) {
			is UnitScript -> ""
			is LinkScript -> link.sayString
		}

val ScriptLink.sayString
	get() =
		lhs.sayString + line.sayString

val ScriptLine.sayString
	get() =
		when (this) {
			is LiteralScriptLine -> literal.sayString
			is FieldScriptLine -> field.sayString
		}

val ScriptField.sayString
	get() =
		string + " " + rhs.sayString

val Literal.sayString
	get() =
		when (this) {
			is StringLiteral -> string
			is NumberLiteral -> number.toString()
		}

