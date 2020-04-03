@file:Suppress("UNCHECKED_CAST")

package leo14.lambda.runtime

typealias Value = Any?
typealias Function = (Value) -> Value

fun fn(function: Function): Value = function
operator fun Value.invoke(value: Value): Value = (this as Function)(value)
