package leo25

data class Function(val resolver: Resolver, val body: Body)

fun Resolver.function(body: Body): Function = Function(this, body)

fun Function.apply(value: Value): Value = resolver.apply(body, value)