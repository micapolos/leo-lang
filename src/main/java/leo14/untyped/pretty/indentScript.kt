package leo14.untyped.pretty

import leo.base.indentString
import leo14.*

fun Script.indentString(indent: Int): String =
	when (this) {
		is UnitScript -> ""
		is LinkScript -> link.indentString(indent)
	}

fun ScriptLink.indentString(indent: Int): String =
	when (lhs) {
		is UnitScript -> line.indentString(indent)
		is LinkScript -> lhs.indentString(indent) + "\n" + indent.indentString + line.indentString(indent)
	}

fun ScriptLine.indentString(indent: Int): String =
	when (this) {
		is LiteralScriptLine -> literal.spacedString
		is FieldScriptLine -> field.indentString(indent)
	}

fun ScriptField.indentString(indent: Int): String {
	val rhsSpacedStringOrNull = rhs.spacedStringOrNull
	if (rhsSpacedStringOrNull != null) {
		if (indent * 2 + string.length + 1 + rhsSpacedStringOrNull.length <= prettyColumns) {
			return "$string $rhsSpacedStringOrNull"
		}
	}

	val rhsCommasStringOrNull = rhs.commasStringOrNull
	if (rhsCommasStringOrNull != null) {
		if (indent * 2 + string.length + 1 + rhsCommasStringOrNull.length + 1 <= prettyColumns) {
			return "$string($rhsCommasStringOrNull)"
		}
	}

	val rhsIndentString = rhs.indentString(indent.inc())
	return string + "\n" + indent.inc().indentString + rhsIndentString
}