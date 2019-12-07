package leo14.lambda.js.expr

import leo14.*
import leo14.js.ast.*
import leo14.lambda.Term
import leo14.lambda.code.Gen
import leo14.lambda.script

data class Set(val lhs: Term<Expr>, val name: String, val rhs: Term<Expr>)

fun Term<Expr>.set(name: String, rhs: Term<Expr>) = Set(this, name, rhs)

fun Set.astExpr(gen: Gen): leo14.js.ast.Expr =
	expr(fn(
		args("x"),
		block(
			expr(id("x.$name")).set(rhs.astExpr(gen)),
			stmt(ret(expr(id("x"))))))).invoke(lhs.astExpr(gen))

val Set.reflectScript: Script
	get() =
		lhs
			.script(Expr::reflectScriptLine)
			.plus("set" lineTo script(name.literal).plus(rhs.script(Expr::reflectScriptLine)))