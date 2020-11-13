package leo14.matching

import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.NumberLiteral
import leo14.ScriptField
import leo14.ScriptLine
import leo14.StringLiteral

val ScriptLine.name: String
	get() =
		when (this) {
			is LiteralScriptLine -> literal.name
			is FieldScriptLine -> field.name
		}

val Literal.name: String
	get() =
		when (this) {
			is StringLiteral -> "text"
			is NumberLiteral -> "number"
		}

val ScriptField.name: String get() = string