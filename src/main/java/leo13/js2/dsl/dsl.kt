package leo13.js2.dsl

import leo13.js2.*

data class Function(val args: Args)

val window = expr(id("window"))
val console = expr(id("console"))

val Expr.log get() = get("log")
val Expr.onload get() = get("onload")

fun function(vararg names: String) = Function(args(*names))
fun Function.returns(vararg exprs: Expr) = expr(Fn(args, seq(*exprs)))