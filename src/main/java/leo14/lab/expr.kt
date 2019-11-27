package leo14.lab

import leo14.lambda.*

sealed class Expr
data class ConstExpr(val int: Int) : Expr()
data class ModExpr(val mod: Mod) : Expr()
data class OpExpr(val binaryOp: Op) : Expr()
data class OpIntExpr(val binaryOp: Op, val int: Int) : Expr()
data class SwitchExpr(val zeroTerm: Term<Expr>, val nonZeroTerm: Term<Expr>) : Expr()

sealed class Mod
object IncMod : Mod()
object DecMod : Mod()
object NegMod : Mod()

sealed class Op
object PlusOp : Op()
object MinusOp : Op()
object TimesOp : Op()
object DivOp : Op()
object ModOp : Op()

fun switchExpr(zeroTerm: Term<Expr>, nonZeroTerm: Term<Expr>): Expr = SwitchExpr(zeroTerm, nonZeroTerm)

val Int.expr: Expr get() = ConstExpr(this)
val Mod.expr: Expr get() = ModExpr(this)
val Op.expr: Expr get() = OpExpr(this)
fun Op.expr(int: Int): Expr = OpIntExpr(this, int)

val incMod: Mod = IncMod
val decMod: Mod = IncMod
val negMod: Mod = IncMod

val plusOp: Op = PlusOp
val minusOp: Op = MinusOp
val timesOp: Op = TimesOp
val divOp: Op = DivOp
val modOp: Op = ModOp

fun Expr.invokeOrNull(expr: Expr): Term<Expr>? =
	when (this) {
		is ConstExpr ->
			when (expr) {
				is ModExpr -> term(expr.mod.invoke(int).expr)
				is OpIntExpr -> term(expr.binaryOp.invoke(int, expr.int).expr)
				is SwitchExpr -> if (int == 0) expr.zeroTerm else expr.nonZeroTerm
				else -> null
			}
		is OpExpr ->
			when (expr) {
				is ConstExpr -> term(binaryOp.expr(expr.int))
				else -> null
			}
		else -> null
	}

fun Mod.invoke(int: Int): Int =
	when (this) {
		IncMod -> int.inc()
		DecMod -> int.dec()
		NegMod -> int.unaryMinus()
	}

fun Op.invoke(lhs: Int, rhs: Int): Int =
	when (this) {
		PlusOp -> lhs + rhs
		MinusOp -> lhs - rhs
		TimesOp -> lhs * rhs
		DivOp -> lhs / rhs
		ModOp -> lhs % rhs
	}

fun Expr.termInvoke(rhs: Value<Expr>): Value<Expr>? =
	invokeOrNull(rhs.term.native)?.let { term -> value(scope(), term) }

val exprEvaluator = evaluator(Expr::termInvoke)

val Term<Expr>.exprEval get() = eval(exprEvaluator)

