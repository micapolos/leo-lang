package leo13.js2

data class Id(val name: String)

fun id(name: String) = Id(name)
val Id.code get() = "$name"