package leo13.js.ast

data class Id(val name: String)

fun id(name: String) = Id(name)
val Id.exprCode get() = "$name"