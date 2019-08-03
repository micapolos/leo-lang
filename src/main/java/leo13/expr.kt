package leo13

import leo9.*

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

val Stack<Op>.expr get() = Expr(this)
fun Expr.plus(op: Op) = opStack.push(op).expr
fun expr(vararg ops: Op) = stack(*ops).expr

fun op(argument: Argument): Op = ArgumentOp(argument)
fun op(access: IntAccess): Op = AccessOp(access)
fun op(link: OpLink): Op = LinkOp(link)
fun op(switch: OpSwitch): Op = SwitchOp(switch)

fun access(int: Int) = IntAccess(int)
fun opLink(exprLine: ExprLine) = OpLink(exprLine)
infix fun Int.lineTo(expr: Expr) = ExprLine(this, expr)

fun switchOp(vararg exprs: Expr) = op(OpSwitch(exprs.toList().reversed()))

// --- eval

fun Expr.eval(bindings: ValueBindings): Value =
	value().fold(opStack.reverse) { op ->
		op.eval(bindings, this)
	}

fun Op.eval(bindings: ValueBindings, lhs: Value): Value =
	when (this) {
		is ArgumentOp -> argument.eval(bindings, lhs)
		is AccessOp -> access.eval(bindings, lhs)
		is LinkOp -> link.eval(bindings, lhs)
		is SwitchOp -> switch.eval(bindings, lhs)
	}

fun Argument.eval(bindings: ValueBindings, lhs: Value) = bindings.stack.drop(previousStack)!!.top
fun IntAccess.eval(bindings: ValueBindings, lhs: Value) = lhs.access(int)
fun OpLink.eval(bindings: ValueBindings, lhs: Value) = lhs.plus(line.int lineTo line.rhs.eval(bindings))
fun OpSwitch.eval(bindings: ValueBindings, lhs: Value) = exprList[lhs.lastLine.int].eval(bindings)

// --- value -> expr

val Value.expr: Expr
	get() =
		lineStack.map { op }.expr

val ValueLine.op
	get() =
		op(opLink(int lineTo rhs.expr))

