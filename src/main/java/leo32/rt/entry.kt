@file:Suppress("unused")

package leo32.rt

import leo.base.ifOrNull
import leo.base.notNullIf
import leo.base.onlySeq
import leo.base.then

data class Entry(
	val key: Value,
	val value: Value)

val Entry.fieldSeq
	get() =
		key.fieldSeq.then { toField.onlySeq }

infix fun Value.to(value: Value) =
	Entry(this, value)

val Entry.toField
	get() =
		toSymbol to value

fun Scope.value(entry: Entry) =
	emptyValue.plus(entry.toField)

fun Entry.at(symbol: Symbol) =
	key.at(symbol).let { keyAt ->
		if (keyAt == null) notNullIf(symbol == toSymbol) { value }
		else ifOrNull(symbol != toSymbol) { keyAt }
	}

fun Scope.at(entry: Entry, symbol: Symbol) =
	entry.at(symbol)

fun Scope.fieldSeq(entry: Entry) =
	entry.fieldSeq
