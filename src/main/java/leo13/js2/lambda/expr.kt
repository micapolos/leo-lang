package leo13.js2.lambda

sealed class Expr<out T>

data class ValueExpr<T>(val value: T) : Expr<T>()
data class ArrowExpr<T>(val arrow: Arrow<Expr<T>>) : Expr<T>()
data class LhsExpr<T>(val lhs: Lhs<Expr<T>>) : Expr<T>()
data class RhsExpr<T>(val rhs: Rhs<Expr<T>>) : Expr<T>()
data class FnExpr<T>(val fn: Fn<Expr<T>>) : Expr<T>()
data class ApExpr<T>(val ap: Ap<Expr<T>>) : Expr<T>()
data class ArgExpr<T>(val arg: Arg) : Expr<T>()

fun <T> expr(value: T): Expr<T> = ValueExpr(value)
fun <T> expr(arrow: Arrow<Expr<T>>): Expr<T> = ArrowExpr(arrow)
fun <T> expr(lhs: Lhs<Expr<T>>): Expr<T> = LhsExpr(lhs)
fun <T> expr(rhs: Rhs<Expr<T>>): Expr<T> = RhsExpr(rhs)
fun <T> expr(fn: Fn<Expr<T>>): Expr<T> = FnExpr(fn)
fun <T> expr(ap: Ap<Expr<T>>): Expr<T> = ApExpr(ap)
fun <T> expr(arg: Arg): Expr<T> = ArgExpr(arg)
