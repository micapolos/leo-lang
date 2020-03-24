package leo14.untyped

import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral

fun Value.select(name: String): Line? =
	when (this) {
		EmptyValue -> null
		is SequenceValue -> sequence.select(name)
	}

fun Thunk.select(name: String): Line? =
	value.select(name)

fun Sequence.select(name: String) =
	null
		?: head.select(name)
		?: tail.select(name)

fun Line.select(name: String) =
	if (selects(name)) this
	else null

fun Line.selects(name: String) =
	when (name) {
		numberName -> this is LiteralLine && literal is NumberLiteral
		textName -> this is LiteralLine && literal is StringLiteral
		functionName -> this is FunctionLine
		nativeName -> this is NativeLine
		else -> this is FieldLine && name == field.name
	}

val Line.selectName
	get() =
		when (this) {
			is LiteralLine -> literal.selectName
			is FieldLine -> field.name
			is FunctionLine -> function.selectName
			is NativeLine -> native.selectName
		}

val Literal.selectName
	get() =
		when (this) {
			is StringLiteral -> textName
			is NumberLiteral -> numberName
		}

val Function.selectName get() = functionName
val Native.selectName get() = nativeName