package leo13.js2

import leo13.js.htmlOpen
import leo13.js.jsInHtml

fun Expr.show() =
	expr(id("document"))
		.get("body")
		.get("textContent")
		.set(this)
		.code.jsInHtml.htmlOpen()

fun Stmt.open() =
	code.jsInHtml.htmlOpen()

fun open(vararg stmts: Stmt) = stmt(block(*stmts)).open()

val String.jsOpen get() = jsInHtml.htmlOpen()
val String.jsShow get() = "document.body.textContent = $this".jsOpen

val Expr.open get() = code.jsOpen
val Expr.show get() = code.jsShow
