package leo13.js

import leo13.Stack
import leo13.stack

data class Call(val lhs: Expression, val name: String, val argStack: Stack<Expression>)

fun Expression.call(name: String, argStack: Stack<Expression>) = Call(this, name, argStack)
fun Expression.call(name: String, vararg args: Expression) = call(name, stack(*args))

val Call.code get() = "${lhs.code}.$name(...)"