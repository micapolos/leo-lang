package leo14.lambda.js.expr

import leo14.js.ast.op
import leo14.lambda.Term
import leo14.lambda.code.Gen
import leo14.js.ast.Expr as AstExpr
import leo14.js.ast.expr as astExpr

data class Op(val lhs: Term<Expr>, val name: String, val rhs: Term<Expr>)

fun Term<Expr>.op(name: String, rhs: Term<Expr>) = Op(this, name, rhs)

fun Op.astExpr(gen: Gen): AstExpr =
	astExpr(lhs.astExpr(gen).op(name, rhs.astExpr(gen)))