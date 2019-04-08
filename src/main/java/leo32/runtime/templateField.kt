package leo32.runtime

data class FunctionField(
	val name: String,
	val value: Template)

infix fun String.to(template: Template) =
	FunctionField(this, template)

fun FunctionField.invoke(term: Term, parameter: Parameter): Term =
	term.plus(name to value.invoke(parameter))

val TermField.functionField get() =
	name to template(value)