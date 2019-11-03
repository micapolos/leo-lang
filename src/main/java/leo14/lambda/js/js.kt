package leo14.lambda.js

import leo13.js.ast.Expr
import leo13.js.ast.id
import leo13.js.ast.invoke
import leo13.js.ast.ret
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
fun Variable<Expr>.expr(gen: Gen) = leo13.js.ast.expr(id(index(gen).varCode))
