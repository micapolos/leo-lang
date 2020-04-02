package leo14.untyped.pretty

import leo.base.ifOrNull
import leo14.*

val Script.spacedStringOrNull: String?
	get() =
		when (this) {
			is UnitScript -> ""
			is LinkScript -> link.spacedStringOrNull
		}

val ScriptLink.spacedStringOrNull: String?
	get() =
		ifOrNull(lhs.isEmpty) {
			line.spacedStringOrNull
		}

val ScriptLine.spacedStringOrNull: String?
	get() =
		when (this) {
			is LiteralScriptLine -> literal.spacedString
			is FieldScriptLine -> field.spacedStringOrNull
		}

val ScriptField.spacedStringOrNull: String?
	get() =
		rhs.spacedStringOrNull?.let { "$string $it" }
