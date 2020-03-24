package leo14.untyped

data class Constant(val value: Value)

fun constant(value: Value) = Constant(value)
val Value.constant get() = Constant(this)