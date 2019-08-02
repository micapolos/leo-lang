package leo13

import leo9.*

object Argument

data class Expr(val opStack: Stack<Op>)
data class ExprLine(val int: Int, val rhs: Expr)

sealed class Op
data class ArgumentOp(val argument: Argument) : Op()
data class AccessOp(val access: IntAccess) : Op()
data class LinkOp(val link: OpLink) : Op()
data class SwitchOp(val switch: OpSwitch) : Op()

data class IntAccess(val int: Int)

data class OpSwitch(val exprList: List<Expr>)

data class OpLink(val line: ExprLine)

// --- constructors

val argument = Argument

fun expr(opStack: Stack<Op>) = Expr(opStack)
fun Expr.plus(op: Op) = expr(opStack.push(op))
fun expr(vararg ops: Op) = expr(stack(*ops))

fun op(argument: Argument): Op = ArgumentOp(argument)
fun op(access: IntAccess): Op = AccessOp(access)
fun op(link: OpLink): Op = LinkOp(link)
fun op(switch: OpSwitch): Op = SwitchOp(switch)

fun access(int: Int) = IntAccess(int)
fun opLink(exprLine: ExprLine) = OpLink(exprLine)
infix fun Int.lineTo(expr: Expr) = ExprLine(this, expr)

fun switchOp(vararg exprs: Expr) = op(OpSwitch(exprs.toList().reversed()))

// --- eval

fun Expr.eval(parameter: Value): Value =
	value().fold(opStack.reverse) { op ->
		op.eval(parameter, this)
	}

fun Op.eval(parameter: Value, lhs: Value): Value =
	when (this) {
		is ArgumentOp -> parameter
		is AccessOp -> access.eval(parameter, lhs)
		is LinkOp -> link.eval(parameter, lhs)
		is SwitchOp -> switch.eval(parameter, lhs)
	}

fun IntAccess.eval(parameter: Value, lhs: Value) = lhs.access(int)
fun OpLink.eval(parameter: Value, lhs: Value) = lhs.plus(line.int lineTo line.rhs.eval(parameter))
fun OpSwitch.eval(parameter: Value, lhs: Value) = exprList[lhs.lastLine.int].eval(parameter)

// --- constant expr from value

fun expr(value: Value): Expr =
	expr(value.lineStack.map(::op))

fun op(valueLine: ValueLine) =
	op(opLink(valueLine.int lineTo expr(valueLine.rhs)))

