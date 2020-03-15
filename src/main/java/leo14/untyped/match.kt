package leo14.untyped

import leo14.*
import leo14.Number

fun Script.matches(script: Script): Boolean =
	if (script == script("anything")) true
	else when (this) {
		is UnitScript -> script is UnitScript
		is LinkScript -> link.matches(script)
	}

fun ScriptLink.matches(script: Script) =
	when (line) {
		is LiteralScriptLine -> null
		is FieldScriptLine ->
			when (line.field.string) {
				"or" -> line.field.rhs.matches(script) || lhs.matches(script)
				else -> null
			}
	} ?: (script is LinkScript && matches(script.link))

fun ScriptLink.matches(scriptLink: ScriptLink) =
	line.matches(scriptLink.line) && lhs.matches(scriptLink.lhs)

fun ScriptLine.matches(scriptLine: ScriptLine) =
	when (this) {
		is LiteralScriptLine -> scriptLine is LiteralScriptLine && literal == scriptLine.literal
		is FieldScriptLine -> field.matches(scriptLine)
	}

fun ScriptField.matches(scriptLine: ScriptLine) =
	when (string) {
		"number" -> scriptLine is LiteralScriptLine && scriptLine.literal is NumberLiteral
		"text" -> scriptLine is LiteralScriptLine && scriptLine.literal is StringLiteral
		else -> scriptLine is FieldScriptLine && matches(scriptLine.field)
	}

fun ScriptField.matches(scriptField: ScriptField) =
	string == scriptField.string && rhs.matches(scriptField.rhs)

fun <R> Script.match(string: String, fn: (Script, Script) -> R): R? =
	when (this) {
		is UnitScript -> null
		is LinkScript -> link.match(string, fn)
	}

fun <R> Script.matchEmpty(fn: () -> R): R? =
	when (this) {
		is UnitScript -> fn()
		is LinkScript -> null
	}

fun <R> Script.matchLink(fn: (ScriptLink) -> R): R? =
	when (this) {
		is UnitScript -> null
		is LinkScript -> fn(link)
	}

fun <R> Script.matchLine(fn: (ScriptLine) -> R): R? =
	matchLink { link ->
		link.matchLine(fn)
	}

fun <R> Script.matchBody(fn: (Script) -> R): R? =
	matchLine { line ->
		line.matchField { field ->
			fn(field.rhs)
		}
	}

fun <R> Script.matchNumber(fn: (Number) -> R): R? =
	matchLine { line ->
		line.matchLiteral { literal ->
			literal.matchNumber(fn)
		}
	}

fun <R> Script.matchString(fn: (String) -> R): R? =
	matchLine { line ->
		line.matchLiteral { literal ->
			literal.matchString(fn)
		}
	}

fun <R> Script.matchName(fn: (String) -> R): R? =
	matchLine { line ->
		line.matchField { field ->
			field.matchName(fn)
		}
	}

fun <R> ScriptLink.matchLine(fn: (ScriptLine) -> R): R? =
	when (lhs) {
		is UnitScript -> fn(line)
		is LinkScript -> null
	}

fun <R> ScriptLink.match(string: String, fn: (Script, Script) -> R): R? =
	line.match(string) { rhs -> fn(lhs, rhs) }

fun <R> ScriptLink.matchSimple(string: String, fn: (Script) -> R): R? =
	match(string) { lhs, rhs ->
		rhs.matchEmpty {
			fn(lhs)
		}
	}

fun <R> ScriptLink.match(string1: String, string2: String, fn: (Script, Script) -> R): R? =
	match(string2) { lhs2, rhs2 ->
		lhs2.match(string1) { lhs1, rhs1 ->
			rhs1.matchEmpty {
				fn(lhs1, rhs2)
			}
		}
	}

fun <R> ScriptLine.match(string: String, fn: (Script) -> R): R? =
	when (this) {
		is LiteralScriptLine -> null
		is FieldScriptLine -> field.match(string, fn)
	}

fun <R> ScriptLine.matchField(fn: (ScriptField) -> R): R? =
	when (this) {
		is LiteralScriptLine -> null
		is FieldScriptLine -> fn(field)
	}

fun <R> ScriptLine.matchLiteral(fn: (Literal) -> R): R? =
	when (this) {
		is LiteralScriptLine -> fn(literal)
		is FieldScriptLine -> null
	}

fun <R> Literal.matchNumber(fn: (Number) -> R): R? =
	when (this) {
		is StringLiteral -> null
		is NumberLiteral -> fn(number)
	}

fun <R> Literal.matchString(fn: (String) -> R): R? =
	when (this) {
		is StringLiteral -> fn(string)
		is NumberLiteral -> null
	}

fun <R> ScriptField.match(string: String, fn: (Script) -> R): R? =
	if (this.string == string) fn(rhs)
	else null

fun <R> ScriptField.matchName(fn: (String) -> R): R? =
	when (rhs) {
		is UnitScript -> fn(string)
		is LinkScript -> null
	}
