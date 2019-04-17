package leo32.runtime

data class FunctionField(
	val name: Symbol,
	val value: Template)

infix fun Symbol.to(template: Template) =
	FunctionField(this, template)

infix fun String.to(template: Template) =
	symbol(this) to template

fun FunctionField.invoke(term: Term, parameter: Parameter): Term =
	term.plus(name to value.invoke(parameter))

val TermField.functionField get() =
	name to template(value)