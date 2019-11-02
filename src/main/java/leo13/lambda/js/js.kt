package leo13.lambda.js

import leo13.js2.*
import leo13.lambda.*
import leo13.lambda.code.Gen
import leo13.lambda.code.gen
import leo13.lambda.code.inc

val Value.expr get() = expr(gen)

fun Value.expr(gen: Gen): Expr =
	when (this) {
		is NativeValue -> native
		is AbstractionValue -> abstraction.expr(gen)
		is ApplicationValue -> application.expr(gen)
		is VariableValue -> variable.expr(gen)
	}

fun Abstraction<Value>.expr(gen: Gen) = paramCode(gen) ret gen.inc { body.expr(it) }
fun Application<Value>.expr(gen: Gen) = lhs.expr(gen).invoke(rhs.expr(gen))
fun Variable<Expr>.expr(gen: Gen) = expr(id(index(gen).varCode))
