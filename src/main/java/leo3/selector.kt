package leo3

data class Selector(
	val template: Template,
	val getter: Getter)

fun selector(template: Template, getter: Getter) =
	Selector(template, getter)

fun Selector.apply(parameter: Parameter): Value =
	getter.apply(template.apply(parameter).nodeOrNull!!)
