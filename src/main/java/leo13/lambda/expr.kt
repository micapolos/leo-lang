package leo13.lambda

sealed class Expr<out T>

data class ValueExpr<T>(val value: T) : Expr<T>()
data class AbstractionExpr<T>(val abstraction: Abstraction<Expr<T>>) : Expr<T>()
data class ApplicationExpr<T>(val application: Application<Expr<T>>) : Expr<T>()
data class VariableExpr<T>(val variable: Variable<T>) : Expr<T>()

fun <T> expr(value: T): Expr<T> = ValueExpr(value)
fun <T> expr(abstraction: Abstraction<Expr<T>>): Expr<T> = AbstractionExpr(abstraction)
fun <T> expr(application: Application<Expr<T>>): Expr<T> = ApplicationExpr(application)
fun <T> expr(variable: Variable<T>): Expr<T> = VariableExpr(variable)
