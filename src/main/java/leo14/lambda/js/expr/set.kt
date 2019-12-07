package leo14.lambda.js.expr

import leo14.Script
import leo14.js.ast.*
import leo14.lambda.Term
import leo14.lambda.code.Gen
import leo14.lambda.script
import leo14.lineTo
import leo14.plus

data class Set(val lhs: Term<Expr>, val rhs: Term<Expr>)

fun Term<Expr>.set(rhs: Term<Expr>) = Set(this, rhs)

fun Set.astExpr(gen: Gen): leo14.js.ast.Expr =
	expr(fn(
		args("x"),
		block(
			lhs.astExpr(gen).set(rhs.astExpr(gen)),
			stmt(ret(expr(id("x"))))))).invoke()

val Set.reflectScript: Script
	get() =
		lhs
			.script(Expr::reflectScriptLine)
			.plus("set" lineTo rhs.script(Expr::reflectScriptLine))