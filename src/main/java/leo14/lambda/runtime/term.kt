@file:Suppress("UNCHECKED_CAST")

package leo14.lambda.runtime

typealias Value = Any?
typealias Fn = (Value) -> Value

fun fn(fn: Fn): Value = fn
operator fun Value.invoke(value: Value): Value = (this as Fn)(value)
