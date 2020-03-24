package leo14.untyped

import leo14.*

fun Script.matches(script: Script): Boolean =
	if (script == script(anythingName)) true
	else when (this) {
		is UnitScript -> script is UnitScript
		is LinkScript -> link.matches(script)
	}

fun ScriptLink.matches(script: Script) =
	when (line) {
		is LiteralScriptLine -> null
		is FieldScriptLine ->
			when (line.field.string) {
				orName -> line.field.rhs.matches(script) || lhs.matches(script)
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
		numberName -> scriptLine is LiteralScriptLine && scriptLine.literal is NumberLiteral
		textName -> scriptLine is LiteralScriptLine && scriptLine.literal is StringLiteral
		else -> scriptLine is FieldScriptLine && matches(scriptLine.field)
	}

fun ScriptField.matches(scriptField: ScriptField) =
	string == scriptField.string && rhs.matches(scriptField.rhs)

fun Thunk.matches(thunk: Thunk): Boolean =
	null
		?: anythingMatches
		?: orMatches(thunk)
		?: eitherMatches(thunk)
		?: rawMatches(thunk)

val Thunk.anythingMatches
	get() =
		matchInfix(anythingName) { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchEmpty {
					true
				}
			}
		}

fun Thunk.orMatches(thunk: Thunk): Boolean? =
	matchInfix(orName) { lhs, rhs ->
		rhs.matches(thunk) || lhs.matches(thunk)
	}

fun Thunk.eitherMatches(thunk: Thunk): Boolean? =
	matchPrefix(eitherName) { rhs ->
		rhs.casesMatch(thunk)
	}

fun Thunk.casesMatch(thunk: Thunk): Boolean =
	value.sequenceOrNull.let { sequenceOrNull ->
		if (sequenceOrNull == null) false
		else sequenceOrNull.head.caseMatches(thunk) || sequenceOrNull.tail.casesMatch(thunk)
	}

fun Line.caseMatches(thunk: Thunk): Boolean =
	thunk.value.onlyLineOrNull?.let { matches(it) } ?: false

fun Thunk.rawMatches(thunk: Thunk) =
	value.matches(thunk.value)

fun Value.matches(value: Value): Boolean =
	when (this) {
		EmptyValue -> value is EmptyValue
		is SequenceValue -> value is SequenceValue && sequence.matches(value)
	}

fun Sequence.matches(value: Value) =
	null
		?: rawMatches(value)

fun Sequence.rawMatches(value: Value) =
	value is SequenceValue && matches(value.sequence)

fun Sequence.matches(sequence: Sequence) =
	head.matches(sequence.head) && tail.matches(sequence.tail)

fun Line.matches(line: Line): Boolean =
	null
		?: numberMatches(line)
		?: textMatches(line)
		?: functionMatches(line)
		?: rawMatches(line)

fun Line.rawMatches(line: Line) =
	when (this) {
		is LiteralLine -> line is LiteralLine && literal == line.literal
		is FieldLine -> line is FieldLine && field.matches(line.field)
		is FunctionLine -> false
		is NativeLine -> false
	}

fun Line.numberMatches(line: Line) =
	if (this == line(numberName)) line is LiteralLine && line.literal is NumberLiteral
	else null

fun Line.textMatches(line: Line) =
	if (this == line(textName)) line is LiteralLine && line.literal is StringLiteral
	else null

fun Line.functionMatches(line: Line) =
	if (this == line(functionName)) line is FunctionLine
	else null

fun Field.matches(field: Field) =
	name == field.name && thunk.matches(field.thunk)

// === name matching ===

fun Value.matches(name: String) =
	when (this) {
		EmptyValue -> name == nothingName
		is SequenceValue -> sequence.matches(name)
	}

fun Sequence.matches(name: String) =
	head.matches(name)

fun Line.matches(name: String) =
	when (this) {
		is LiteralLine -> literal.matches(name)
		is FieldLine -> field.matches(name)
		is FunctionLine -> name == functionName
		is NativeLine -> false
	}

fun Field.matches(name: String) =
	this.name == name

fun Literal.matches(name: String) =
	when (this) {
		is StringLiteral -> name == textName
		is NumberLiteral -> name == numberName
	}
