package leo14.untyped.pretty

import leo14.*

val Script.commasStringOrNull: String?
	get() =
		when (this) {
			is UnitScript -> ""
			is LinkScript -> link.commasStringOrNull
		}

val ScriptLink.commasStringOrNull: String?
	get() =
		when (lhs) {
			is UnitScript -> line.commasStringOrNull
			is LinkScript -> lhs.commasStringOrNull?.let { lhsString ->
				line.commasStringOrNull?.let { rhsString ->
					"$lhsString, $rhsString"
				}
			}
		}

val ScriptLine.commasStringOrNull: String?
	get() =
		when (this) {
			is LiteralScriptLine -> literal.spacedString
			is FieldScriptLine -> field.commasStringOrNull
		}

val ScriptField.commasStringOrNull: String?
	get() {
		val rhsSpacedStringOrNull = rhs.spacedStringOrNull
		if (rhsSpacedStringOrNull != null) {
			return "$string $rhsSpacedStringOrNull"
		}
		return null
	}
