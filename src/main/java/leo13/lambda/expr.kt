package leo13.lambda

sealed class Expr<out T>

data class ValueExpr<T>(val value: T) : Expr<T>()
data class FnExpr<T>(val fn: Fn<Expr<T>>) : Expr<T>()
data class ApExpr<T>(val ap: Ap<Expr<T>>) : Expr<T>()
data class ArgExpr<T>(val arg: Arg<T>) : Expr<T>()

fun <T> expr(value: T): Expr<T> = ValueExpr(value)
fun <T> expr(fn: Fn<Expr<T>>): Expr<T> = FnExpr(fn)
fun <T> expr(ap: Ap<Expr<T>>): Expr<T> = ApExpr(ap)
fun <T> expr(arg: Arg<T>): Expr<T> = ArgExpr(arg)
