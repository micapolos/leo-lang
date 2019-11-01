package leo13.js2.dsl

import leo13.js2.*

data class Function(val args: Args)

val window = expr(id("window"))
val console = expr(id("console"))
val event = expr(id("event"))

val Expr.log get() = get("log")
val Expr.onload get() = get("onload")
val Expr.onkeypress get() = get("keypress")

fun fn(vararg names: String) = Function(args(*names))

infix fun Function.ret(expr: Expr) = expr(Fn(args, seq(expr)))
fun Function.does(vararg exprs: Expr) = expr(Fn(args, seq(*exprs)))
