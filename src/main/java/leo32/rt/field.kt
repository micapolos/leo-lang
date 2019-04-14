@file:Suppress("unused")

package leo32.rt

import leo.base.Seq
import leo.base.notNullIf
import leo.base.seq

data class Field(
	val symbol: Symbol,
	val value: Value)

infix fun Symbol.to(value: Value) =
	Field(this, value)

fun Field.at(symbol: Symbol) =
	notNullIf(this.symbol == symbol) { value }

fun Scope.at(field: Field, symbol: Symbol) =
	field.at(symbol)

fun Scope.field(field: Field) =
	field

fun Scope.fieldSeq(field: Field) =
	seq(field)

val Field.byteSeq: Seq<Byte>
	get() =
		symbol.fieldByteSeq { value.byteSeq }
