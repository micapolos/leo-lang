package leo14.lambda.value

import leo14.lambda.Term

fun <T> Term<T>.value(nativeApply: NativeApply<T>): Value<T> =
	emptyScope<T>().value(this, nativeApply)

val Term<Any>.value: Value<Any>
	get() =
		value(Any::anyApply)
