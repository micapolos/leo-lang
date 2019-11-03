package leo13.js.ast

sealed class Expr

data class NilExpr(val nil: Null) : Expr()
data class StringExpr(val string: String) : Expr()
data class IntExpr(val int: Int) : Expr()
data class DoubleExpr(val double: Double) : Expr()
data class FnExpr(val fn: Fn) : Expr()
data class ApExpr(val ap: Ap) : Expr()
data class GetExpr(val get: Get) : Expr()
data class AtExpr(val at: At) : Expr()
data class IdExpr(val id: Id) : Expr()
data class ArrExpr(val arr: Arr) : Expr()
data class LambdaExpr(val lambda: Lambda) : Expr()

fun expr(nil: Null): Expr = NilExpr(nil)
fun expr(string: String): Expr = StringExpr(string)
fun expr(int: Int): Expr = IntExpr(int)
fun expr(double: Double): Expr = DoubleExpr(double)
fun expr(fn: Fn): Expr = FnExpr(fn)
fun expr(ap: Ap): Expr = ApExpr(ap)
fun expr(get: Get): Expr = GetExpr(get)
fun expr(at: At): Expr = AtExpr(at)
fun expr(id: Id): Expr = IdExpr(id)
fun expr(arr: Arr): Expr = ArrExpr(arr)
fun expr(lambda: Lambda): Expr = LambdaExpr(lambda)

val Expr.code: String
	get() =
		when (this) {
			is NilExpr -> nil.exprCode
			is StringExpr -> "'$string'"
			is IntExpr -> "$int"
			is DoubleExpr -> "$double"
			is FnExpr -> fn.exprCode
			is ApExpr -> ap.exprCode
			is GetExpr -> get.exprCode
			is AtExpr -> at.exprCode
			is IdExpr -> id.exprCode
			is ArrExpr -> arr.exprCode
			is LambdaExpr -> lambda.exprCode
		}

val Expr.stmtCode get() = code

val nullExpr get() = expr(id("null"))
fun expr(boolean: Boolean) = expr(id("$boolean"))