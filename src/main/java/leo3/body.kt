package leo3

data class Body(
	val function: Function,
	val template: Template)

fun body(function: Function, template: Template) =
	Body(function, template)
