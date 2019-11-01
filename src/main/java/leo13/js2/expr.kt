package leo13.js2

sealed class Expr

data class NilExpr(val nil: Nil) : Expr()
data class StringExpr(val string: String) : Expr()
data class IntExpr(val int: Int) : Expr()
data class DoubleExpr(val double: Double) : Expr()
data class FnExpr(val fn: Fn) : Expr()
data class ApExpr(val ap: Ap) : Expr()
data class GetExpr(val get: Get) : Expr()
data class AtExpr(val at: At) : Expr()
data class IdExpr(val id: Id) : Expr()

fun expr(nil: Nil): Expr = NilExpr(nil)
fun expr(string: String): Expr = StringExpr(string)
fun expr(int: Int): Expr = IntExpr(int)
fun expr(double: Double): Expr = DoubleExpr(double)
fun expr(fn: Fn): Expr = FnExpr(fn)
fun expr(ap: Ap): Expr = ApExpr(ap)
fun expr(get: Get): Expr = GetExpr(get)
fun expr(at: At): Expr = AtExpr(at)
fun expr(id: Id): Expr = IdExpr(id)

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
		}

val Expr.stmtCode get() = code