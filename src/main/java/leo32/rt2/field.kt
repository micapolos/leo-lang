package leo32.rt2

import leo.base.empty
import leo.base.notNullIf
import leo32.rt.Symbol
import leo32.rt.argumentSymbol

data class Field(
	val symbol: Symbol,
	val value: Value)

infix fun Symbol.to(value: Value) =
	Field(this, value)

val Field.isSimple
	get() =
		value.isEmpty

fun Field.at(symbol: Symbol) =
	notNullIf(this.symbol == symbol) { value }

fun field(symbol: Symbol): Field =
	symbol to empty.scope.emptyValue

fun Field.invoke(scope: Scope) =
	symbol to scope.invoke(value)

val argumentField = field(argumentSymbol)
