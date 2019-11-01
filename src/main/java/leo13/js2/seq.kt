package leo13.js2

import leo13.*
import leo13.base.linesString

data class Seq(val stack: Stack<Expr>)

fun seq(vararg exprs: Expr) = Seq(stack(*exprs))

val Seq.code
	get() =
		when (stack) {
			is EmptyStack -> nil.code
			is LinkStack -> when (stack.link.stack) {
				is EmptyStack -> stack.link.value.code
				is LinkStack -> linesString(
					"function() {",
					returnCode,
					"}()")
			}
		}

val Seq.returnCode
	get() =
		when (stack) {
			is EmptyStack -> ""
			is LinkStack -> stack.link.stack.toList()
				.map { it.code }
				.plus("return ${stack.link.value.code};")
				.joinToString("; ")
		}