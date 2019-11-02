package leo13.lambda

fun <T> fn(body: Expr<T>) = expr(abstraction(body))
fun <T> fn2(body: Expr<T>) = fn(fn(body))
fun <T> fn3(body: Expr<T>) = fn(fn2(body))

operator fun <T> Expr<T>.invoke(expr: Expr<T>) = expr(application(this, expr))

fun <T> arg(index: Int): Expr<T> = expr(variable(index))
fun <T> arg0(): Expr<T> = arg(0)
fun <T> arg1(): Expr<T> = arg(1)
fun <T> arg2(): Expr<T> = arg(2)

fun <T> first(): Expr<T> = fn2(arg(1))
fun <T> second(): Expr<T> = fn2(arg(0))

fun <T> pair(lhs: Expr<T>, rhs: Expr<T>) = fn3(arg<T>(0)(arg(2))(arg(1)))(lhs)(rhs)

val <T> Expr<T>.first get() = this(first())
val <T> Expr<T>.second get() = this(second())
