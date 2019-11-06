package leo14.js.ast

import leo13.Stack
import leo13.stack
import leo13.toList

data class Arr(val exprs: Stack<Expr>)

fun arr(vararg exprs: Expr) = Arr(stack(*exprs))
val Arr.exprCode get() = "[${exprs.toList().joinToString(",") { it.code }}]"
