package leo32.rt2

import leo.base.*
import leo32.base.List
import leo32.base.list
import leo32.base.seq
import leo32.rt.Symbol
import leo32.rt.quoteSymbol

data class Value(
	val scope: Scope,
	val fieldList: List<Field>,
	val scriptOrNull: Script?,
	val type: Type)

val Scope.emptyValue
	get() =
		Value(this, empty.list(), null, selfType)

val Empty.value
	get() =
		scope.emptyValue

fun value(vararg fields: Field) =
	empty.value.fold(fields) { invoke(it) }

val Value.isEmpty
	get() =
		scriptOrNull == null

val Value.clear
	get() =
		scope.emptyValue

fun Value.at(symbol: Symbol): Value? =
	scriptOrNull?.at(symbol)

fun Value.invoke(value: Value) =
	fold(value.fieldList.seq) { invoke(it) }

fun Value.invoke(field: Field): Value =
	field.invoke(scope).let { invokedField ->
		null
			?: invokeWrap(invokedField)
			?: invokeResolved(invokedField)
	}

fun Value.invokeResolved(field: Field): Value =
	null
		?: invokeArgument(field)
		?: invokeAt(field)
		?: invokeWrap(field)
		?: invokePlus(field)

fun Value.invokeArgument(field: Field) =
	notNullIf(field == argumentField) {
		scope.argument
	}

fun Value.invokeWrap(field: Field) =
	notNullIf(field.isSimple) {
		clear.invoke(field.symbol to this)
	}

fun Value.invokeAt(field: Field) =
	scriptOrNull?.let { script ->
		ifOrNull(script.isSimple) {
			script.value.at(field.symbol)
		}
	}

fun Value.invokeQuote(field: Field) =
	notNullIf(field.symbol == quoteSymbol) {

	}

fun Value.invokePlus(field: Field): Value =
	copy(scriptOrNull = plus(field)) // TODO types!!!

fun value(symbol: Symbol) =
	empty.value.invoke(symbol to empty.value)