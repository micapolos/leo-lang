package leo14.lambda.evaluator

import leo14.lambda.Term

data class Function<out T>(val scope: Scope<T>, val term: Term<T>)

fun <T> Scope<T>.function(term: Term<T>): Function<T> = Function(this, term)

fun <T> Function<T>.apply(value: Value<T>, nativeApply: NativeApply<T>): Value<T> =
	scope.push(value).value(term, nativeApply)