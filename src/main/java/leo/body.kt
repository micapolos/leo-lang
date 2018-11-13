package leo

// TODO: What about recursion?
data class Body(
	val template: Template,
	val function: Function)

fun body(template: Template, function: Function) =
	Body(template, function)

fun Body.apply(argument: Script): Script =
	function.invoke(template.apply(argument))

val Body.reflect: Field<Nothing>
	get() =
		bodyWord fieldTo term(
			template.reflect,
			function.reflect)