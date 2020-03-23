package leo14.untyped

import leo14.Literal
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
		numberName -> this is LiteralValue && literal is NumberLiteral
		textName -> this is LiteralValue && literal is StringLiteral
		functionName -> this is FunctionValue
		nativeName -> this is NativeValue
		else -> this is FieldValue && name == field.name
	}

val Value.selectName
	get() =
		when (this) {
			is LiteralValue -> literal.selectName
			is FieldValue -> field.name
			is FunctionValue -> function.selectName
			is NativeValue -> native.selectName
		}

val Literal.selectName
	get() =
		when (this) {
			is StringLiteral -> textName
			is NumberLiteral -> numberName
		}

val Function.selectName get() = functionName
val Native.selectName get() = nativeName