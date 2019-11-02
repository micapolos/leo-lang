package leo13.lambda.js

import leo13.js2.Expr
import leo13.js2.expr
import leo13.js2.id
import leo13.lambda.code.Code
import leo13.lambda.value

typealias Value = leo13.lambda.Value<Expr>

fun arg(index: Int): Value = leo13.lambda.arg(index)
val arg0 get() = arg(0)
val arg1 get() = arg(1)
val arg2 get() = arg(2)

fun value(code: Code) = value(expr(id(code.string)))
fun value(int: Int) = value(expr(int))
fun value(double: Double) = value(expr(double))
fun value(string: String) = value(expr(string))
