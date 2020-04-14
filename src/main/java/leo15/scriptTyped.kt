package leo15

import leo14.*

val Script.typed: Typed
	get() =
		when (this) {
			is UnitScript -> emptyTyped
			is LinkScript -> link.typed
		}

val ScriptLink.typed: Typed
	get() =
		lhs.typed.plus(line.typedLine)

val ScriptLine.typedLine: TypedLine
	get() =
		when (this) {
			is LiteralScriptLine -> literal.staticTypedLine
			is FieldScriptLine -> field.typedLine
		}

val ScriptField.typedLine: TypedLine
	get() =
		string lineTo rhs.typed
