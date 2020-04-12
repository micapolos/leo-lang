package leo14.untyped.typed

import leo14.*

val Script.staticType: Type
	get() =
		when (this) {
			is UnitScript -> emptyType
			is LinkScript -> link.staticType
		}

val ScriptLink.staticType: Type
	get() =
		lhs.staticType.plus(line.staticTypeLine)

val ScriptLine.staticTypeLine: TypeLine
	get() =
		when (this) {
			is LiteralScriptLine -> literal.typeLine
			is FieldScriptLine -> field.staticTypeLine
		}

val ScriptField.staticTypeLine: TypeLine
	get() =
		string lineTo rhs.staticType
