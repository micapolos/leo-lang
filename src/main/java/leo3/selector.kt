package leo3

data class Selector(
	val template: Template,
	val getter: Getter)

fun selector(template: Template, getter: Getter) =
	Selector(template, getter)

fun Selector.apply(value: Value): Value =
	template
		.apply(value)
		.fullScript
		.let { script -> value(getter.apply(script)) }
