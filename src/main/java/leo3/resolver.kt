package leo3

data class Resolver(val fn: () -> Value)

fun resolver(fn: () -> Value) = Resolver(fn)