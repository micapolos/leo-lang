@file:Suppress("UNCHECKED_CAST")

package leo14.lambda.runtime

typealias Value = Any?
typealias Fn = (Value) -> Value

fun valueFn(fn: Fn): Value = fn
fun Value.valueInvoke(value: Value): Value = (this as Fn)(value)

fun fn(fn: Fn): Value = valueFn(fn)
operator fun Value.invoke(value: Value): Value = valueInvoke(value)
