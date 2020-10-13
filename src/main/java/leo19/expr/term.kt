package leo19.expr

import leo19.term.NullTerm
import leo19.term.Term
import leo19.term.function
import leo19.term.get
import leo19.term.invoke
import leo19.term.term
import leo19.term.variable

val Expr.term: Term
	get() =
		when (this) {
			NullExpr -> NullTerm
			is IntExpr -> term(int)
			is ArrayExpr -> term(*list.map { it.term }.toTypedArray())
			is ArrayGetExpr -> term(array.term).get(index.term)
			is FunctionExpr -> term(function(body.term))
			is InvokeExpr -> term(function.term).invoke(param.term)
			is VariableExpr -> term(variable(index))
		}