package leo14.lambda.js

import leo14.Literal
import leo14.NumberLiteral
import leo14.StringLiteral
import leo14.expr
import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.js.ast.invoke
import leo14.js.ast.ret
import leo14.lambda.*
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
fun Variable<Expr>.expr(gen: Gen) = expr(leo14.js.ast.id(index(gen).varCode))

val Literal.expr: Expr
	get() =
		when (this) {
			is StringLiteral -> expr(string)
			is NumberLiteral -> number.expr
		}
