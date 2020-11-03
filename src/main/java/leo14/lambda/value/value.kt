package leo14.lambda.value

import leo14.lambda.Term
import leo14.lambda.term

sealed class Value<out T>

data class NativeValue<T>(val native: T) : Value<T>()
data class FunctionValue<T>(val function: Function<T>) : Value<T>()

fun <T> value(native: T): Value<T> = NativeValue(native)
fun <T> value(function: Function<T>): Value<T> = FunctionValue(function)

fun <T> Value<T>.apply(value: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	when (this) {
		is NativeValue -> native.nativeApply(value)
		is FunctionValue -> function.apply(value, nativeApply)
	}

val <T> Value<T>.term: Term<T>
	get() =
		when (this) {
			is NativeValue -> term(native)
			is FunctionValue -> function.term
		}

fun Any.anyApply(value: Value<Any>): Value<Any> =
	value((this as (Any) -> Any).invoke((value as NativeValue).native))
