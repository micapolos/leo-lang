package leo13.lambda.js

import leo13.js2.*
import leo13.lambda.*

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

fun arg(index: Int): Value = leo13.lambda.arg(index)
val arg0 get() = arg(0)
val arg1 get() = arg(1)
val arg2 get() = arg(2)

fun idValue(string: String) = value(expr(id(string)))
fun value(int: Int) = value(expr(int))
fun value(double: Double) = value(expr(double))
fun value(string: String) = value(expr(string))
