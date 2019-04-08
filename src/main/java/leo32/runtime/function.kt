package leo32.runtime

data class Function(
	val type: Type,
	val template: Template)

fun Type.gives(template: Template) =
	Function(this, template)

fun Function.invoke(parameter: Parameter): Term =
	template.invoke(parameter)