package leo13.script

data class Get(val name: String)

fun get(name: String) = Get(name)