package leo13.js2

import leo13.Stack
import leo13.stack
import leo13.toList

data class Params(val stack: Stack<Expr>)

fun params(vararg params: Expr) = Params(stack(*params))
val Params.code get() = stack.toList().joinToString(", ") { it.code }