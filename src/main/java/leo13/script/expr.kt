package leo13.script

import leo.base.orNull
import leo13.Script
import leo13.ScriptLine
import leo13.lineTo
import leo13.plus
import leo13.script
import leo9.*
import leo9.fold

data class Expr(val opStack: Stack<Op>)
data class ExprLine(val name: String, val rhs: Expr)

val Stack<Op>.expr get() = Expr(this)
fun expr(vararg ops: Op) = stack(*ops).expr
fun Expr.plus(op: Op) = opStack.push(op).expr
infix fun String.lineTo(rhs: Expr) = ExprLine(this, rhs)

val Expr.isEmpty get() = opStack.isEmpty

val Script.expr: Expr
	get() =
		expr().fold(lineStack.reverse) {
			plus(op(it.exprLine))
		}

fun expr(script: Script) =
	script.expr

val ScriptLine.exprLine: ExprLine
	get() =
		name lineTo rhs.expr

val Expr.scriptOrNull: Script?
	get() =
		script().orNull.fold(opStack.reverse) { op ->
			op.lineOrNull?.scriptLineOrNull?.let { scriptLine ->
				this?.plus(scriptLine)
			}
		}

val ExprLine.scriptLineOrNull: ScriptLine?
	get() =
		rhs.scriptOrNull?.let { rhsScript ->
			name lineTo rhsScript
		}

fun Script.eval(bindings: Bindings, expr: Expr): Script =
	fold(expr.opStack.reverse) { op ->
		op.eval(bindings, this)
	}

fun Expr.eval(bindings: Bindings): Script =
	script().eval(bindings, this)
