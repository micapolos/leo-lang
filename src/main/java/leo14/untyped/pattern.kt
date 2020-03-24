package leo14.untyped

data class Pattern(val value: Value)

fun pattern(value: Value) = Pattern(value)

fun Pattern.matches(thunk: Thunk) =
	this.value.matches(thunk)

fun Pattern.matches(value: Value) =
	this.value.matches(value)

val recursePattern =
	pattern(value(recurseName))
