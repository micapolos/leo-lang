package leo13.lambda.expr

import leo13.lambda.Expr
import leo13.lambda.expr
import leo13.lambda.lhs
import leo13.lambda.rhs

fun <T> arrow(lhs: Expr<T>, rhs: Expr<T>): Expr<T> = expr(leo13.lambda.arrow(lhs, rhs))
val <T> Expr<T>.lhs get() = expr(lhs(this))
val <T> Expr<T>.rhs get() = expr(rhs(this))

fun <T> utilLhs(): Expr<T> = fn2(arg(1))
fun <T> utilRhs(): Expr<T> = fn2(arg(0))
fun <T> utilArrow(lhs: Expr<T>, rhs: Expr<T>) = fn3(arg<T>(0)(arg(2))(arg(1)))(lhs)(rhs)
val <T> Expr<T>.utilLhs get() = invoke(utilLhs())
val <T> Expr<T>.utilRhs get() = invoke(utilRhs())
