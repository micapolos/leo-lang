package leo16.base

data class Push<Value>(val value: Value)

val <Value> Value.push get() = Push(this)
fun <Value> push(value: Value) = Push(value)
