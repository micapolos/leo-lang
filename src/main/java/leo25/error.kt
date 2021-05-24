package leo25

import leo14.literal

data class ValueError(val value: Value) : Error() {
	override fun toString(): String = value.errorValue.toString()
}

val Value.error get() = ValueError(this)

val Throwable.valueOrNull: Value?
	get() =
		(this as? ValueError)?.value

val Throwable.value: Value
	get() =
		valueOrNull?.errorValue ?: stackTraceValue.errorValue

fun Throwable.causeStackTrace(value: Value): Value =
	value(
		errorName fieldTo value
			.plus(causeName fieldTo stackTraceValue)
	)

val Throwable.stackTraceValue: Value
	get() =
		value(*stackTrace.map { field(literal(it.toString())) }.toTypedArray())

val Value.errorValue: Value
	get() =
		value(errorName fieldTo this)

fun <T> Value.throwError(): T =
	throw error

fun <T> T?.notNullOrThrow(fn: () -> Value): T =
	this ?: fn().throwError()