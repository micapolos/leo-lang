package leo32.rt

import leo.base.empty
import leo.base.flat
import leo.base.map
import leo32.base.I32

data class Value(
	val scope: Scope,
	val term: Term,
	val type: Type)

val Value.clear
	get() =
		copy(term = term(empty))

val Scope.emptyValue
	get() =
		Value(this, term(empty), selfType)

fun Scope.value(i32: I32) =
	Value(this, I32Term(i32), emptyValue.plus(i32Symbol to emptyValue).asType)

fun Value.plus(symbol: Symbol, fn: Value.() -> Value): Value = TODO()

fun Value.plus(field: Field): Value = TODO()

fun Value.at(symbol: Symbol) =
	scope.at(term, symbol)

val Value.fieldSeq
	get() =
		scope.fieldSeq(term)

val Value.byteSeq
	get() =
		fieldSeq.map { byteSeq }.flat

val Value.bitSeq
	get() =
		byteSeq.byteBitSeq

fun value(term: Term) = Value(empty.scope, term, selfType)
fun value(symbol: Symbol) = value(term(symbol))

val i32Value = value(i32Symbol)
