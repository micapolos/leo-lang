package leo5

data class Plus(val name: String, val function: Function)

fun plus(name: String, function: Function) = Plus(name, function)