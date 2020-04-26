package leo15.lambda.runtime.type

data class Typed<V, T>(val value: Value<V>, val type: Type<T>)

infix fun <V, T> Value<V>.of(type: Type<T>) = Typed(this, type)
