package leo32.runtime

data class Call(
	val template: Template)

fun call(template: Template) =
	Call(template)

fun Call.invoke(term: Term) =
	template.invoke(parameter(term))