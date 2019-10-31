package leo13.js

import leo.base.ifOrNull
import leo.base.orIfNull
import leo13.Stack
import leo13.stack
import leo13.toList

data class Call(val lhs: Expression, val name: String, val argStack: Stack<Expression>)

fun Expression.call(name: String, argStack: Stack<Expression>) = Call(this, name, argStack)
fun Expression.call(name: String, vararg args: Expression) = call(name, stack(*args))

val Call.code
	get() =
		ifOrNull(lhs == nullExpression) { "" }
			.orIfNull { "${lhs.code}." }
			.plus("$name(${argStack.toList().joinToString(", ") { it.code }})")
