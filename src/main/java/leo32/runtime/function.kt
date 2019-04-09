package leo32.runtime

data class Function(
	val parameterTypeTerm: Term,
	val returnTypeTerm: Term,
	val template: Template)

fun function(parameterTypeTerm: Term, returnTypeTerm: Term, template: Template) =
	Function(parameterTypeTerm, returnTypeTerm, template)

fun Function.invoke(parameter: Parameter): Term =
	template.invoke(parameter)