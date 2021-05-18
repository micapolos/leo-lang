package leo25

import leo14.*

val Script.valueOrNull: Value?
	get() =
		when (this) {
			is UnitScript -> null
			is LinkScript -> link.value
		}

val ScriptLink.value: Value
	get() =
		lhs.valueOrNull.plus(line.stringValueOrNullPair)

val ScriptLine.stringValueOrNullPair: Pair<String, Value?>
	get() =
		when (this) {
			is FieldScriptLine -> field.stringValueOrNullPair
			is LiteralScriptLine -> literal.stringValueOrNullPair
		}

val ScriptField.stringValueOrNullPair: Pair<String, Value?>
	get() =
		string to rhs.valueOrNull

val Literal.stringValueOrNullPair: Pair<String, Value?>
	get() =
		when (this) {
			is NumberLiteral -> "number" to value(number.toString())
			is StringLiteral -> "text" to value(string)
		}
