package leo32.interpreter

import leo32.runtime.Term
import leo32.runtime.invoke
import leo32.runtime.map
import leo32.runtime.parameter

data class Scope(
	val types: Types,
	val functions: Functions)

fun Scope.invoke(term: Term): Term =
	term.map { invoke(this) }.let { mappedTerm ->
		functions.at(types.at(mappedTerm)).invoke(parameter(mappedTerm))
	}
