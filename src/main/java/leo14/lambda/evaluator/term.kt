package leo14.lambda.evaluator

import leo14.lambda.Term

fun <T> Term<T>.value(nativeApply: NativeApply<T>): Value<T> =
	emptyScope<T>().value(this, nativeApply)

fun <T> Term<T>.evaluate(nativeApply: NativeApply<T>): Term<T> =
	value(nativeApply).term

val <T> Term<T>.value: Value<T>
	get() =
		value { error("nativeApply") }

val <T> Term<T>.evaluate: Term<T>
	get() =
		value.term