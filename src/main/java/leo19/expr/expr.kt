package leo19.expr

sealed class Expr
object NullExpr : Expr()
data class IntExpr(val int: Int) : Expr()
data class PairExpr(val lhs: Expr, val rhs: Expr) : Expr()
data class LhsExpr(val pair: Expr) : Expr()
data class RhsExpr(val pair: Expr) : Expr()
data class ArrayExpr(val list: List<Expr>) : Expr()
data class ArrayGetExpr(val array: Expr, val index: Expr) : Expr()
data class FunctionExpr(val body: Expr, val isRecursive: Boolean) : Expr()
data class InvokeExpr(val function: Expr, val param: Expr) : Expr()
data class VariableExpr(val index: Int) : Expr()
data class EqualsExpr(val lhs: Expr, var rhs: Expr) : Expr()