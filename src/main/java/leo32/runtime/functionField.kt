package leo32.runtime

import leo.base.empty

data class FunctionField(
	val name: String,
	val value: Function)

infix fun String.fieldTo(function: Function) =
	FunctionField(this, function)

val String.fieldToEmptyFunction get() =
	this fieldTo empty.function

fun FunctionField.invoke(term: Term, parameter: Parameter): Term =
	term.plus(name fieldTo value.invoke(parameter))
