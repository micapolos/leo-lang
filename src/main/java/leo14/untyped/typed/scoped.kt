package leo14.untyped.typed

import leo14.lambda.runtime.Value

data class Scoped(val scope: Scope, val value: Value)

fun scoped(scope: Scope, value: Value) = Scoped(scope, value)
fun Scope.with(value: Value) = scoped(this, value)
