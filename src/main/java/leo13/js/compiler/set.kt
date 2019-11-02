package leo13.js.compiler

data class Set(val lhs: Expression, val name: String, val rhs: Expression)

fun Expression.set(name: String, rhs: Expression) = Set(this, name, rhs)

val Set.code get() = "function() { ${lhs.code}.$name = ${rhs.code}; return null; }()"