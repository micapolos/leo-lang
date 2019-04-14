package leo32.rt2

import leo.base.notNullIf
import leo32.rt.Symbol

data class Script(
	val value: Value,
	val field: Field)

fun Script.at(symbol: Symbol) =
	field.at(symbol).let { at ->
		if (at == null) value.at(symbol)
		else notNullIf(value.at(symbol) == null) { value }
	}

fun Value.plus(field: Field) =
	Script(this, field)

val Script.isSimple
	get() =
		value.isEmpty
