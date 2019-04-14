package leo32.rt

import leo.base.empty

data class Type(
	val valueOrNull: Value?)

val selfType = Type(null)

val Value.asType
	get() =
		Type(this)

fun type(symbol: Symbol) =
	Value(empty.scope, term(symbol), selfType)