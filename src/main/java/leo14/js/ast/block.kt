package leo13.js.ast

import leo13.Stack
import leo13.isEmpty
import leo13.stack
import leo13.toList

data class Block(val stack: Stack<Stmt>)

fun block(vararg stmts: Stmt) = Block(stack(*stmts))

val Block.code
	get() =
		if (stack.isEmpty) "{}"
		else "{" + stack.toList().joinToString(";") { it.code } + ";}"

val Block.stmtCode
	get() =
		"if (true) $code"
