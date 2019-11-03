package leo14.lambda

import leo13.Index

sealed class Value<out T>

data class NativeValue<T>(val native: T) : Value<T>()
data class AbstractionValue<T>(val abstraction: Abstraction<Value<T>>) : Value<T>()
data class ApplicationValue<T>(val application: Application<Value<T>>) : Value<T>()
data class VariableValue<T>(val variable: Variable<T>) : Value<T>()

fun <T> value(native: T): Value<T> = NativeValue(native)
fun <T> value(abstraction: Abstraction<Value<T>>): Value<T> = AbstractionValue(abstraction)
fun <T> value(application: Application<Value<T>>): Value<T> = ApplicationValue(application)
fun <T> value(variable: Variable<T>): Value<T> = VariableValue(variable)

// === matching

fun <T, R> Value<T>.abstraction(fn: (Value<T>) -> R): R =
	when (this) {
		is AbstractionValue -> fn(abstraction.body)
		else -> error("abstraction expected")
	}

fun <T, R> Value<T>.application(fn: (Value<T>, Value<T>) -> R): R =
	when (this) {
		is ApplicationValue -> fn(application.lhs, application.rhs)
		else -> error("application expected")
	}

fun <T, R> Value<T>.variable(fn: (Index) -> R): R =
	when (this) {
		is VariableValue -> fn(variable.index)
		else -> error("variable expected")
	}

fun <T, R> Value<T>.native(fn: (T) -> R): R =
	when (this) {
		is NativeValue -> fn(native)
		else -> error("native expected")
	}

