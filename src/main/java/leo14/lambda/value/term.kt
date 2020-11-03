package leo14.lambda.value

import leo14.lambda.Term

fun <T> Term<T>.value(nativeApply: NativeApply<T>): Value<T> =
	emptyScope<T>().value(this, nativeApply)

fun <T> Term<T>.evaluate(nativeApply: NativeApply<T>): Term<T> =
	value(nativeApply).term

val Term<Any>.value: Value<Any>
	get() =
		value(Any::anyApply)

val Term<Any>.evaluate: Term<Any>
	get() =
		value.term