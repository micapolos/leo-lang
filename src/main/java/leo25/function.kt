package leo25

data class Function(val context: Context, val body: Body)
data class Body(val value: Value)

fun body(value: Value) = Body(value)

fun Function.apply(value: Value): Value =
	context
		.plusGiven(value)
		.resolve(body.value)
