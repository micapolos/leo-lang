package leo13.value

import leo.base.orNull
import leo13.script.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.script
import leo9.*
import leo9.fold

data class Expr(val opStack: Stack<Op>) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "expr"
	override val scriptableBody get() = opStack.asScript { asScriptLine }
}

data class ExprLine(val name: String, val rhs: Expr) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "line"
	override val scriptableBody get() = script(name lineTo script(), "to" lineTo script(rhs.scriptableLine))
}

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

val String.unsafeExpr
	get() =
		unsafeScript.expr

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
