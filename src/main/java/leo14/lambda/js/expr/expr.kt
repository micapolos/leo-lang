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

val Literal.expr: Expr get() = LiteralExpr(this)
val Code.expr: Expr get() = CodeExpr(this)
val Op.expr: Expr get() = OpExpr(this)
val Get.expr: Expr get() = GetExpr(this)
val Expr.term: Term<Expr> get() = term(this)

fun Expr.astExpr(gen: Gen): leo14.js.ast.Expr =
	when (this) {
		is LiteralExpr -> literal.expr
		is CodeExpr -> code.astExpr
		is OpExpr -> op.astExpr(gen)
		is GetExpr -> get.astExpr(gen)
	}

val Expr.astExpr: leo14.js.ast.Expr
	get() =
		astExpr(gen)

val Expr.reflectScriptLine: ScriptLine
	get() =
		"expr" lineTo script(
			when (this) {
				is LiteralExpr -> literal.reflectScriptLine
				is CodeExpr -> code.reflectScriptLine
				is OpExpr -> op.reflectScriptLine
				is GetExpr -> get.reflectScriptLine
			})