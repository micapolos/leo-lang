package leo14.lambda

import leo13.*

data class Scope<T>(
	val valueStack: Stack<Value<T>>,
	val nativeApply: NativeApply<T>)

fun <T> emptyScope(nativeApply: NativeApply<T> = errorNativeApply()): Scope<T> =
	Scope(stack(), nativeApply)

fun <T> Scope<T>.push(value: Value<T>) = Scope(valueStack.push(value), nativeApply)

operator fun <T> Scope<T>.get(index: Index) = valueStack.get(index)!!
