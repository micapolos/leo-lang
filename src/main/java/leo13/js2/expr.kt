package leo13.js2

sealed class Expr

data class NilExpr(val nil: Nil) : Expr()
data class StringExpr(val string: String) : Expr()
data class IntExpr(val int: Int) : Expr()
data class DoubleExpr(val double: Double) : Expr()
data class FnExpr(val fn: Fn) : Expr()
data class ApExpr(val ap: Ap) : Expr()
data class GetExpr(val get: Get) : Expr()
data class SetExpr(val set: Set) : Expr()
data class AtExpr(val at: At) : Expr()
data class SeqExpr(val seq: Seq) : Expr()
data class IdExpr(val id: Id) : Expr()

fun expr(nil: Nil): Expr = NilExpr(nil)
fun expr(string: String): Expr = StringExpr(string)
fun expr(int: Int): Expr = IntExpr(int)
fun expr(double: Double): Expr = DoubleExpr(double)
fun expr(fn: Fn): Expr = FnExpr(fn)
fun expr(ap: Ap): Expr = ApExpr(ap)
fun expr(get: Get): Expr = GetExpr(get)
fun expr(set: Set): Expr = SetExpr(set)
fun expr(at: At): Expr = AtExpr(at)
fun expr(seq: Seq): Expr = SeqExpr(seq)
fun expr(id: Id): Expr = IdExpr(id)

val Expr.code: String
	get() =
		when (this) {
			is NilExpr -> nil.code
			is StringExpr -> "'$string'"
			is IntExpr -> "$int"
			is DoubleExpr -> "$double"
			is FnExpr -> fn.code
			is ApExpr -> ap.code
			is GetExpr -> get.code
			is SetExpr -> set.code
			is AtExpr -> at.code
			is SeqExpr -> seq.code
			is IdExpr -> id.code
		}

val Expr.returnCode
	get() =
		if (this is SeqExpr) seq.returnCode
		else "return $code;"