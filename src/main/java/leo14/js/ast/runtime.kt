package leo14.js.ast

import leo14.js.compiler.htmlOpen
import leo14.js.compiler.jsInHtml

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
val String.jsPrint get() = "document.body.textContent=$this".jsOpen
val String.jsShow get() = "document.body.appendChild($this)".jsOpen

val Expr.open get() = code.jsOpen
val Expr.print get() = code.jsPrint
val Expr.show get() = code.jsShow
