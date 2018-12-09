package leo.lab.v2

data class Body(
	val function: Function,
	val template: Template)

fun Function.body(template: Template): Body =
	Body(this, template)

val Template.body: Body
	get() =
		identityFunction.body(this)

fun Body.invoke(script: Script): Script? =
	template.invoke(script)?.let { templatedScript ->
		function.invoke(templatedScript)
	}