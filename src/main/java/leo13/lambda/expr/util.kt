package leo13.lambda.expr

import leo13.lambda.Expr

fun <T> first(): Expr<T> = fn2(arg(1))
fun <T> second(): Expr<T> = fn2(arg(0))

fun <T> pair(lhs: Expr<T>, rhs: Expr<T>) = fn3(arg<T>(0)(arg(2))(arg(1)))(lhs)(rhs)

val <T> Expr<T>.first get() = this(first())
val <T> Expr<T>.second get() = this(second())
