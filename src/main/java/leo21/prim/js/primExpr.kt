package leo21.prim.js

import leo14.js.ast.Expr
import leo14.js.ast.NULL
import leo14.js.ast.expr
import leo14.js.ast.id
import leo14.js.ast.invoke
import leo14.js.ast.lambda
import leo14.js.ast.op
import leo14.lambda.runtime.invoke
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.NilPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim

val Prim.expr: Expr
	get() =
		when (this) {
			is NilPrim -> expr(NULL)
			is StringPrim -> expr(string)
			is DoublePrim -> expr(double)
			DoublePlusDoublePrim -> op2Expr("+")
			DoubleMinusDoublePrim -> op2Expr("-")
			DoubleTimesDoublePrim -> op2Expr("*")
			StringPlusStringPrim -> op2Expr("*")
		}

val lhsExpr = expr(lambda("a", expr(lambda("b", expr(id("a"))))))
val rhsExpr = expr(lambda("a", expr(lambda("b", expr(id("b"))))))

fun op2Expr(op: String): Expr = expr(lambda("x", expr(expr(id("x")).invoke(lhsExpr).op(op, expr(id("x")).invoke(rhsExpr)))))
