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

fun Program.matches(program: Program): Boolean =
	null
		?: anythingMatches
		?: rawMatches(program)

val Program.anythingMatches
	get() =
		matchInfix(anythingName) { lhs, rhs ->
			rhs.matchEmpty {
				lhs.matchEmpty {
					true
				}
			}
		}

fun Program.rawMatches(program: Program) =
	when (this) {
		EmptyProgram -> program is EmptyProgram
		is SequenceProgram -> program is SequenceProgram && sequence.match(program)
	}

fun Sequence.match(program: Program) =
	null
		?: orMatches(program)
		?: rawMatches(program)

fun Sequence.orMatches(program: Program) =
	head.match(orName) { rhs ->
		rhs.matches(program) || tail.matches(program)
	}

fun Sequence.rawMatches(program: Program) =
	program is SequenceProgram && matches(program.sequence)

fun Sequence.matches(sequence: Sequence) =
	head.matches(sequence.head) && tail.matches(sequence.tail)

fun Value.matches(value: Value) =
	null
		?: numberMatches(value)
		?: textMatches(value)
		?: functionMatches(value)
		?: rawMatches(value)

fun Value.rawMatches(value: Value) =
	when (this) {
		is LiteralValue -> value is LiteralValue && literal == value.literal
		is FieldValue -> value is FieldValue && field.matches(value.field)
		is FunctionValue -> value is FunctionValue && function == value.function
		is NativeValue -> value is NativeValue && native == value.native
	}

fun Value.numberMatches(value: Value) =
	if (this == value(numberName)) value is LiteralValue && value.literal is NumberLiteral
	else null

fun Value.textMatches(value: Value) =
	if (this == value(textName)) value is LiteralValue && value.literal is StringLiteral
	else null

fun Value.functionMatches(value: Value) =
	if (this == value(functionName)) value is FunctionValue
	else null

fun Field.matches(field: Field) =
	name == field.name && rhs.matches(field.rhs)

// === name matching ===

fun Program.matches(name: String) =
	when (this) {
		EmptyProgram -> name == nothingName
		is SequenceProgram -> sequence.matches(name)
	}

fun Sequence.matches(name: String) =
	head.matches(name)

fun Value.matches(name: String) =
	when (this) {
		is LiteralValue -> literal.matches(name)
		is FieldValue -> field.matches(name)
		is FunctionValue -> name == functionName
		is NativeValue -> false
	}

fun Field.matches(name: String) =
	this.name == name

fun Literal.matches(name: String) =
	when (this) {
		is StringLiteral -> name == textName
		is NumberLiteral -> name == numberName
	}
