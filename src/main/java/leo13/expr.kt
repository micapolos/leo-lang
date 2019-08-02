package leo13

import leo.base.Empty

sealed class Expr
data class EmptyExpr(val empty: Empty) : Expr()
data class ArgumentExpr(val argument: Argument) : Expr()
data class ExecExpr(val apply: ExprApply) : Expr()

data class ExprApply(val lhs: Expr, val op: Op)

object Argument

data class ExprLine(val int: Int, val expr: Expr)

// --- constructors

fun expr(empty: Empty): Expr = EmptyExpr(empty)
fun expr(argument: Argument): Expr = ArgumentExpr(argument)
fun expr(exec: ExprApply): Expr = ExecExpr(exec)

fun exec(lhs: Expr, op: Op) = ExprApply(lhs, op)
val argument = Argument

infix fun Int.lineTo(expr: Expr) = ExprLine(this, expr)

// --- eval

fun Expr.eval(parameter: Value): Value =
	when (this) {
		is EmptyExpr -> empty.eval(parameter)
		is ArgumentExpr -> argument.eval(parameter)
		is ExecExpr -> apply.eval(parameter)
	}

fun Empty.eval(parameter: Value): Value = value()
fun Argument.eval(parameter: Value): Value = parameter
fun ExprApply.eval(parameter: Value): Value = op.eval(parameter, lhs.eval(parameter))

fun ExprLine.eval(parameter: Value) = int lineTo expr.eval(parameter)

// --- constant expr from value

fun expr(value: Value) = value.expr

val Value.expr: Expr
	get() =
		when (this) {
			is EmptyValue -> expr(empty)
			is LinkValue -> link.expr
		}

val ValueLink.expr
	get() =
		expr(exec(lhs.expr, op(opLink(line.exprLine))))

val ValueLine.exprLine
	get() =
		int lineTo rhs.expr

// --- script + pattern -> expr

