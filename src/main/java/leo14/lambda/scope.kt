package leo14.lambda

import leo13.*

data class Scope<T>(val valueStack: Stack<Value<T>>)

val <T> Stack<Value<T>>.scope get() = Scope(this)
fun <T> scope(vararg values: Value<T>) = stack(*values).scope
fun <T> Scope<T>.push(value: Value<T>) = valueStack.push(value).scope
operator fun <T> Scope<T>.get(index: Index): Value<T>? = valueStack.get(index)
