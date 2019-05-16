package leo3

data class Selector(
	val template: Template,
	val getter: Getter)

fun selector(template: Template, getter: Getter) =
	Selector(template, getter)

fun Selector.apply(parameter: Parameter): Result? =
	template.apply(parameter)?.run { getter.apply(parameter(termOrNull)) }