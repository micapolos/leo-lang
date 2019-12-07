package leo14.lambda.js.expr

import leo14.code.Code
import leo14.lambda.Term
import leo14.lambda.code.Gen
import leo14.lambda.term

sealed class Expr

data class CodeExpr(val code: Code) : Expr()
data class OpExpr(val op: Op) : Expr()
data class GetExpr(val get: Get) : Expr()

val Code.expr: Expr get() = CodeExpr(this)
val Op.expr: Expr get() = OpExpr(this)
val Get.expr: Expr get() = GetExpr(this)
val Expr.term: Term<Expr> get() = term(this)

fun Expr.astExpr(gen: Gen): leo14.js.ast.Expr =
	when (this) {
		is CodeExpr -> code.astExpr
		is OpExpr -> op.astExpr(gen)
		is GetExpr -> get.astExpr(gen)
	}
