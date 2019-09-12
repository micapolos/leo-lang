package leo13.untyped.expression

data class Get(val name: String)

fun get(name: String) = Get(name)