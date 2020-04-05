package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Scoped(val scope: Scope, val value: Value)

fun scoped(scope: Scope, value: Value) = Scoped(scope, value)
fun Scope.with(value: Value) = scoped(this, value)

fun Scoped.apply(scoped: Scoped): Scoped =
	TODO()

fun Scoped.bind(typed: Typed): Scoped =
	TODO()