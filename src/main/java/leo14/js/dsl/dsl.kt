package leo14.js.dsl

import leo14.js.ast.*

data class Function(val args: Args)

val window = expr(id("window"))
val document = expr(id("document"))
val console = expr(id("console"))
val event = expr(id("event"))

val Expr.alert get() = get("alert")
val Expr.body get() = get("body")
val Expr.textContent get() = get("textContent")
val Expr.log get() = get("log")
val Expr.onload get() = get("onload")
val Expr.onkeypress get() = get("onkeypress")
val Expr.code get() = get("code")

fun js(int: Int) = expr(int)
fun js(double: Double) = expr(double)
fun js(string: String) = expr(string)

fun fn(vararg names: String) = Function(args(*names))

infix fun Function.returns(expr: Expr) = expr(Fn(args, block(stmt(ret(expr)))))
fun Function.does(vararg stmts: Stmt) = expr(Fn(args, block(*stmts)))
val Expr.run get() = stmt(this)

fun main() = open(
	window.alert(js("jajeczko")).run,
	document.body.textContent set js("Loaded..."),
	window.onkeypress set fn("event").does(
		document.body.textContent set event.code,
		console.log(event.code).run))