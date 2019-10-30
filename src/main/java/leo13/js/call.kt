package leo13.js

data class Call(val lhs: Expression, val name: String, val args: List<Expression>)

fun Expression.call(name: String, vararg args: Expression) = Call(this, name, listOf(*args))

val Call.code get() = "${lhs.code}.$name(${args.joinToString(separator = ", ") { it.code }})"