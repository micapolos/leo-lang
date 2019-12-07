package leo14.lambda.js.expr

import leo14.*
import leo14.code.Code
import leo14.code.reflectScriptLine
import leo14.lambda.Term
import leo14.lambda.code.Gen
import leo14.lambda.code.gen
import leo14.lambda.js.expr
import leo14.lambda.term

sealed class Expr

data class LiteralExpr(val literal: Literal) : Expr()
data class CodeExpr(val code: Code) : Expr()
data class OpExpr(val op: Op) : Expr()
data class GetExpr(val get: Get) : Expr()
data class SetExpr(val set: Set) : Expr()
data class InvokeExpr(val invoke: Invoke) : Expr()

val Literal.expr: Expr get() = LiteralExpr(this)
val Code.expr: Expr get() = CodeExpr(this)
val Op.expr: Expr get() = OpExpr(this)
val Get.expr: Expr get() = GetExpr(this)
val Invoke.expr: Expr get() = InvokeExpr(this)
val Set.expr: Expr get() = SetExpr(this)
val Expr.term: Term<Expr> get() = term(this)

fun Expr.astExpr(gen: Gen): leo14.js.ast.Expr =
	when (this) {
		is LiteralExpr -> literal.expr
		is CodeExpr -> code.astExpr
		is OpExpr -> op.astExpr(gen)
		is GetExpr -> get.astExpr(gen)
		is InvokeExpr -> invoke.astExpr(gen)
		is SetExpr -> set.astExpr(gen)
	}

val Expr.astExpr: leo14.js.ast.Expr
	get() =
		astExpr(gen)

val Expr.reflectScriptLine: ScriptLine
	get() =
		"expr" lineTo reflectScript

val Expr.reflectScript: Script
	get() =
		when (this) {
			is LiteralExpr -> script(literal.reflectScriptLine)
			is CodeExpr -> script(code.reflectScriptLine)
			is OpExpr -> op.reflectScript
			is GetExpr -> get.reflectScript
			is InvokeExpr -> invoke.reflectScript
			is SetExpr -> set.reflectScript
		}
