package leo21.prim.js

import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.js.ast.id
import leo14.js.ast.lambda
import leo14.js.ast.op
import leo21.prim.DoubleMinusDoublePrim
import leo21.prim.DoublePlusDoublePrim
import leo21.prim.DoublePrim
import leo21.prim.DoubleTimesDoublePrim
import leo21.prim.MinusDoublePrim
import leo21.prim.PlusDoublePrim
import leo21.prim.PlusStringPrim
import leo21.prim.Prim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim
import leo21.prim.TimesDoublePrim

val Prim.expr: Expr
	get() =
		when (this) {
			is StringPrim -> expr(string)
			is DoublePrim -> expr(double)
			DoublePlusDoublePrim -> op2Expr("+")
			DoubleMinusDoublePrim -> op2Expr("-")
			DoubleTimesDoublePrim -> op2Expr("*")
			StringPlusStringPrim -> op2Expr("*")
			is PlusDoublePrim -> op2Expr(expr(double), "+")
			is MinusDoublePrim -> op2Expr(expr(double), "-")
			is TimesDoublePrim -> op2Expr(expr(double), "*")
			is PlusStringPrim -> op2Expr(expr(string), "+")
		}

fun op2Expr(op: String): Expr = expr(lambda("a", expr(lambda("b", expr(expr(id("a")).op(op, expr(id("b"))))))))
fun op2Expr(lhs: Expr, op: String): Expr = expr(lambda("a", expr(lhs.op(op, expr(id("b"))))))