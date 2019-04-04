package leo32.runtime

data class FunctionField(
	val name: String,
	val value: Function)

infix fun String.to(function: Function) =
	FunctionField(this, function)

fun FunctionField.invoke(term: Term, parameter: Parameter): Term =
	term.plus(name to value.invoke(parameter))
