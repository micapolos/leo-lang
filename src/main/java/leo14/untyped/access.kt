package leo14.untyped

import leo14.*

fun Script.access(string: String): ScriptLine? =
	when (this) {
		is UnitScript -> null
		is LinkScript -> TODO()
	}

fun ScriptLink.access(string: String) =
	when (lhs) {
		is UnitScript -> line.access(string)
		is LinkScript -> null
	}

fun ScriptLine.access(string: String) =
	when (this) {
		is LiteralScriptLine -> null
		is FieldScriptLine -> field.rhs.lineOrNull(string)
	}

