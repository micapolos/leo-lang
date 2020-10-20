package leo19.term.expr

import leo13.map
import leo13.toList
import leo19.expr.ArrayExpr
import leo19.expr.ArrayGetExpr
import leo19.expr.EqualsExpr
import leo19.expr.Expr
import leo19.expr.FunctionExpr
import leo19.expr.IntExpr
import leo19.expr.InvokeExpr
import leo19.expr.LhsExpr
import leo19.expr.NullExpr
import leo19.expr.PairExpr
import leo19.expr.RhsExpr
import leo19.expr.VariableExpr
import leo19.term.ArrayGetTerm
import leo19.term.ArrayTerm
import leo19.term.EqualsTerm
import leo19.term.FunctionTerm
import leo19.term.IntTerm
import leo19.term.InvokeTerm
import leo19.term.LhsTerm
import leo19.term.NullTerm
import leo19.term.PairTerm
import leo19.term.RhsTerm
import leo19.term.Term
import leo19.term.VariableTerm

val Term.expr: Expr
	get() =
		when (this) {
			NullTerm -> NullExpr
			is IntTerm -> IntExpr(int)
			is PairTerm -> PairExpr(lhs.expr, rhs.expr)
			is LhsTerm -> LhsExpr(pair.expr)
			is RhsTerm -> RhsExpr(pair.expr)
			is ArrayTerm -> ArrayExpr(stack.map { expr }.toList())
			is ArrayGetTerm -> ArrayGetExpr(tuple.expr, index.expr)
			is FunctionTerm -> FunctionExpr(function.body.expr, isRecursive = function.isRecursive)
			is InvokeTerm -> InvokeExpr(function.expr, param.expr)
			is VariableTerm -> VariableExpr(variable.index)
			is EqualsTerm -> EqualsExpr(lhs.expr, rhs.expr)
		}

fun expr(int: Int): Expr = IntExpr(int)