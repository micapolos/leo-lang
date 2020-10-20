package leo19.value

import leo13.fold
import leo13.reverse
import leo19.expr.ArrayExpr
import leo19.expr.Expr
import leo19.expr.IntExpr
import leo19.expr.InvokeExpr
import leo19.expr.LhsExpr
import leo19.expr.NullExpr
import leo19.expr.PairExpr
import leo19.expr.RhsExpr

val Value.expr: Expr
	get() =
		when (this) {
			NullValue -> NullExpr
			is IntValue -> IntExpr(int)
			is PairValue -> PairExpr(lhs.expr, rhs.expr)
			is LhsValue -> LhsExpr(pair.expr)
			is RhsValue -> RhsExpr(pair.expr)
			is ArrayValue -> ArrayExpr(list.map { it.expr })
			is FunctionValue -> function.body.fold(function.scope.stack.reverse) { InvokeExpr(this, it.expr) }
		}
