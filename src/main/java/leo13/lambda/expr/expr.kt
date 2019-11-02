package leo13.lambda.expr

import leo13.lambda.Expr
import leo13.lambda.ap
import leo13.lambda.expr

fun <T> fn(body: Expr<T>) = expr(leo13.lambda.fn(body))
fun <T> fn2(body: Expr<T>) = fn(fn(body))
fun <T> fn3(body: Expr<T>) = fn(fn2(body))

operator fun <T> Expr<T>.invoke(expr: Expr<T>) = expr(ap(this, expr))
fun <T> arg(index: Int): Expr<T> = expr(leo13.lambda.arg(index))
