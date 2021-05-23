package leo25

data class Function(val resolver: Resolver, val body: Body)

fun Resolver.function(body: Body): Function = Function(this, body)

fun Function.applyLeo(value: Value): Leo<Value> = resolver.applyLeo(body, value)