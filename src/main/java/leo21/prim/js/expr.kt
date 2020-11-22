package leo21.prim.js

import leo14.js.ast.Expr
import leo14.js.ast.NULL
import leo14.js.ast.expr
import leo14.js.ast.get
import leo14.js.ast.id
import leo14.js.ast.invoke
import leo14.js.ast.lambda
import leo14.js.ast.op
import leo21.prim.NumberCosinusPrim
import leo21.prim.NumberMinusNumberPrim
import leo21.prim.NumberPlusNumberPrim
import leo21.prim.NumberPrim
import leo21.prim.NumberSinusPrim
import leo21.prim.NumberTimesNumberPrim
import leo21.prim.NilPrim
import leo21.prim.NumberEqualsNumberPrim
import leo21.prim.NumberStringPrim
import leo21.prim.Prim
import leo21.prim.StringLengthPrim
import leo21.prim.StringPlusStringPrim
import leo21.prim.StringPrim

val Prim.expr: Expr
	get() =
		when (this) {
			is NilPrim -> expr(NULL)
			is StringPrim -> expr(string)
			is NumberPrim -> expr(number.bigDecimal.toDouble())
			NumberPlusNumberPrim -> op2Expr("+")
			NumberMinusNumberPrim -> op2Expr("-")
			NumberTimesNumberPrim -> op2Expr("*")
			NumberEqualsNumberPrim -> op2Expr("==")
			StringPlusStringPrim -> op2Expr("*")
			NumberSinusPrim -> prefix1Expr("Math.sin")
			NumberCosinusPrim -> prefix1Expr("Math.cos")
			StringLengthPrim -> postfix1Expr("length")
			NumberStringPrim -> TODO()
		}

val lhsExpr = expr(lambda("a", expr(lambda("b", expr(id("a"))))))
val rhsExpr = expr(lambda("a", expr(lambda("b", expr(id("b"))))))

fun op2Expr(op: String): Expr = expr(lambda("x", expr(expr(id("x")).invoke(lhsExpr).op(op, expr(id("x")).invoke(rhsExpr)))))
fun prefix1Expr(op: String): Expr = expr(lambda("x", expr(id(op)).invoke(expr(id("x")))))
fun postfix1Expr(op: String): Expr = expr(lambda("x", expr(id("x")).get(op)))
