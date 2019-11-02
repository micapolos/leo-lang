package leo13.lambda.java

import leo13.lambda.Expr
import leo13.lambda.expr

fun arg(index: Int): Expr<Java> = leo13.lambda.arg(index)
val arg0 get() = arg(0)
val arg1 get() = arg(1)
val arg2 get() = arg(2)

fun expr(int: Int) = expr(java(int))
fun expr(double: Double) = expr(java(double))
fun expr(string: String) = expr(java(string))
