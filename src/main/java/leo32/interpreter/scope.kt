package leo32.interpreter

import leo.base.Empty
import leo32.runtime.Term
import leo32.runtime.invoke
import leo32.runtime.parameter

data class Scope(
	val types: Types,
	val functions: Functions)

val Empty.scope get() =
	Scope(types, functions)

fun Scope.invoke(term: Term): Term =
	functions
		.at(types.at(term))
		.invoke(parameter(term))
