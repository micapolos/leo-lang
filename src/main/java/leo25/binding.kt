package leo25

sealed class Binding
data class ValueBinding(val value: Value) : Binding()
data class FunctionBinding(val function: Function) : Binding()

fun binding(value: Value): Binding = ValueBinding(value)
fun binding(function: Function): Binding = FunctionBinding(function)

fun Binding.apply(given: Value): Value =
	when (this) {
		is FunctionBinding -> function.apply(given)
		is ValueBinding -> value
	}
