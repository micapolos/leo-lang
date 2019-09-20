package leo13.value

import leo.base.fold
import leo13.LeoObject
import leo13.fold
import leo13.reverse
import leo13.script.*

sealed class Expr : LeoObject() {
	override fun toString() = scriptableLine.toString()
	override val scriptableName get() = "expr"
	override val scriptableBody get() = script(exprScriptableLine)
	abstract val exprScriptableName: String
	abstract val exprScriptableBody: Script
	val exprScriptableLine get() = exprScriptableName lineTo exprScriptableBody
}

data class ValueExpr(val value: Value) : Expr() {
	override fun toString() = super.toString()
	override val exprScriptableName get() = value.scriptableName
	override val exprScriptableBody get() = value.scriptableBody
}

data class GivenExpr(val given: Given) : Expr() {
	override fun toString() = super.toString()
	override val exprScriptableName get() = given.scriptableName
	override val exprScriptableBody get() = given.scriptableBody
}

data class LinkExpr(val link: ExprLink) : Expr() {
	override fun toString() = super.toString()
	override val exprScriptableName get() = link.scriptableName
	override val exprScriptableBody get() = link.scriptableBody
}

fun expr(value: Value): Expr = ValueExpr(value)
fun expr(given: Given): Expr = GivenExpr(given)
fun expr(link: ExprLink): Expr = LinkExpr(link)
fun expr() = expr(value())

fun expr(vararg ops: Op): Expr = expr(value(), *ops)
fun expr(value: Value, vararg ops: Op): Expr = expr(value).fold(ops) { plus(it) }
fun expr(given: Given, vararg ops: Op): Expr = expr(given).fold(ops) { plus(it) }

fun Expr.plus(op: Op) = expr(link(this, op))

val Expr.valueOrNull: Value? get() = (this as? ValueExpr)?.value
val Expr.isEmpty get() = valueOrNull?.isEmpty ?: false
val Expr.linkOrNull get() = (this as? LinkExpr)?.link

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

fun expr(name: String) =
	expr(value(name))

val ScriptLine.exprLine: ExprLine
	get() =
		name lineTo rhs.expr
