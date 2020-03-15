package leo14.untyped

import leo14.NumberLiteral
import leo14.StringLiteral

fun Program.select(name: String): Value? =
	when (this) {
		EmptyProgram -> null
		is SequenceProgram -> sequence.select(name)
	}

fun Sequence.select(name: String) =
	null
		?: head.select(name)
		?: tail.select(name)

fun Value.select(name: String) =
	if (selects(name)) this
	else null

fun Value.selects(name: String) =
	when (name) {
		"number" -> this is LiteralValue && literal is NumberLiteral
		"text" -> this is LiteralValue && literal is StringLiteral
		"function" -> this is FunctionValue
		else -> this is FieldValue && name == field.name
	}
