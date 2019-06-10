package leo5

data class Plus(val name: String, val body: Body)

fun plus(name: String, body: Body) = Plus(name, body)