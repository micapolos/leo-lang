package leo32.runtime

data class Function(
	val returnTypeTerm: Term,
	val template: Template)

fun function(returnTypeTerm: Term, template: Template) =
	Function(returnTypeTerm, template)

fun Function.invoke(parameter: Parameter): Term =
	template.invoke(parameter)
