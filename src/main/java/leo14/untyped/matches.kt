package leo14.untyped

import leo.base.notNullIf
import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral

fun Thunk.matches(thunk: Thunk): Boolean =
	null
		?: anythingMatches
		?: nonAnythingMatches(thunk)

val Thunk.anythingMatches
	get() =
		matchInfix(anythingName) { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchEmpty {
					true
				}
			}
		}

fun Thunk.nonAnythingMatches(rhsThunk: Thunk): Boolean =
	rhsThunk.strictValueOrNull
		?.let { matches(it) }
		?: false

fun Thunk.matches(rhsValue: Value): Boolean =
	null
		?: eitherMatches(rhsValue)
		?: rawMatches(rhsValue)


fun Thunk.eitherMatches(rhsValue: Value): Boolean? =
	value.sequenceOrNull?.onlyValueOrNull?.fieldOrNull?.let { field ->
		notNullIf(field.name == eitherName) {
			field.thunk.casesMatch(rhsValue)
		}
	}

fun Thunk.casesMatch(rhsValue: Value): Boolean =
	value.sequenceOrNull.let { sequenceOrNull ->
		if (sequenceOrNull == null) false
		else sequenceOrNull.lastLine.caseMatches(rhsValue) || sequenceOrNull.previousThunk.casesMatch(rhsValue)
	}

fun Line.caseMatches(rhsValue: Value): Boolean =
	rhsValue.onlyStrictLineOrNull?.let { matches(it) } ?: false

fun Thunk.rawMatches(value: Value) =
	this.value.matches(value)

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
	lastLine.matches(sequence.lastLine) && previousThunk.matches(sequence.previousThunk)

fun Line.matches(line: Line): Boolean =
	null
		?: numberMatches(line)
		?: textMatches(line)
		?: nativeMatches(line)
		?: doingMatches(line)
		?: rawMatches(line)

fun Line.rawMatches(line: Line) =
	when (this) {
		is LiteralLine -> line is LiteralLine && literal == line.literal
		is FieldLine -> line is FieldLine && field.matches(line.field)
		is DoingLine -> false
		is NativeLine -> false
	}

fun Line.numberMatches(line: Line) =
	if (this == line(numberName)) line is LiteralLine && line.literal is NumberLiteral
	else null

fun Line.textMatches(line: Line) =
	if (this == line(textName)) line is LiteralLine && line.literal is StringLiteral
	else null

fun Line.nativeMatches(line: Line) =
	if (this == line(nativeName)) line is NativeLine
	else null

fun Line.doingMatches(line: Line) =
	if (this == line(doingName)) line is DoingLine
	else null

fun Field.matches(field: Field) =
	name == field.name && thunk.matches(field.thunk)

// === name matching ===

fun Value.matches(name: String) =
	when (this) {
		EmptyValue -> false
		is SequenceValue -> sequence.matches(name)
	}

fun Sequence.matches(name: String) =
	lastLine.matches(name)

fun Line.matches(name: String) =
	when (this) {
		is LiteralLine -> literal.matches(name)
		is FieldLine -> field.matches(name)
		is DoingLine -> name == doingName
		is NativeLine -> false
	}

fun Field.matches(name: String) =
	this.name == name

fun Literal.matches(name: String) =
	when (this) {
		is StringLiteral -> name == textName
		is NumberLiteral -> name == numberName
	}
