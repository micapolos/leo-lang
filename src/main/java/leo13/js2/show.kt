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
