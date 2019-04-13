package leo32.runtime

data class Function(
	val template: Template,
	val returnTypeTerm: Term)

infix fun Template.of(type: Term) =
	Function(this, type)

fun Function.invoke(parameter: Parameter): Term =
	template.invoke(parameter)

