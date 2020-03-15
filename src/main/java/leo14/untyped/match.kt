package leo14.untyped

import leo14.*
import leo14.Number

// ======================================

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

// === program stuff ===

fun <R> Program.matchEmpty(fn: () -> R): R? =
	(this as? EmptyProgram)?.let { fn() }

fun <R> Program.matchSequence(fn: (Sequence) -> R): R? =
	(this as? SequenceProgram)?.sequence?.let(fn)

fun <R> Program.match(name: String, fn: (Program, Program) -> R) =
	matchSequence { sequence ->
		sequence.match(name, fn)
	}

fun <R> Program.matchName(name: String, fn: () -> R) =
	matchSequence { sequence ->
		sequence.match(name) { lhs, rhs ->
			lhs.matchEmpty {
				rhs.matchEmpty {
					fn()
				}
			}
		}
	}

fun <R> Sequence.match(name: String, fn: (Program, Program) -> R) =
	head.match(name) { rhs ->
		fn(tail, rhs)
	}

fun <R> Value.matchField(fn: (Field) -> R): R? =
	(this as? FieldValue)?.field?.let(fn)

fun <R> Value.match(string: String, fn: (Program) -> R): R? =
	(this as? FieldValue)?.field?.let { field ->
		field.match(string, fn)
	}

fun <R> Field.match(name: String, fn: (Program) -> R) =
	if (this.name == name) fn(rhs)
	else null