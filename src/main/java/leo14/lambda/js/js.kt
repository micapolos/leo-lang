package leo14.lambda.js

import leo14.*
import leo14.js.ast.*
import leo14.lambda.*
import leo14.lambda.Variable
import leo14.lambda.code.Gen
import leo14.lambda.code.gen
import leo14.lambda.code.inc

val Term.expr get() = expr(gen)

fun Term.expr(gen: Gen): Expr =
	when (this) {
		is NativeTerm -> native
		is AbstractionTerm -> abstraction.expr(gen)
		is ApplicationTerm -> application.expr(gen)
		is VariableTerm -> variable.expr(gen)
	}

fun Abstraction<Term>.expr(gen: Gen) = paramCode(gen) ret gen.inc { body.expr(it) }
fun Application<Term>.expr(gen: Gen) = lhs.expr(gen).invoke(rhs.expr(gen))
fun Variable<Expr>.expr(gen: Gen) = expr(id(index(gen).varCode))

val Literal.expr: Expr
	get() =
		when (this) {
			is StringLiteral -> expr(string)
			is NumberLiteral -> number.expr
		}

val Expr.literalOrNull: Literal?
	get() =
		when (this) {
			is StringExpr -> literal(string)
			is IntExpr -> literal(int)
			is DoubleExpr -> literal(double)
			else -> null
		}