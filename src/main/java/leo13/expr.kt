package leo13

import leo.base.fold
import leo.base.orNull
import leo9.*

data class Expr(val opStack: Stack<Op>)
data class ExprLine(val int: Int, val rhs: Expr)

sealed class Op
data class ArgumentOp(val argument: Argument) : Op()
data class AccessOp(val access: IntAccess) : Op()
data class LinkOp(val link: OpLink) : Op()
data class SwitchOp(val switch: OpSwitch) : Op()
data class CallOp(val call: OpCall) : Op()

data class IntAccess(val int: Int)

data class OpSwitch(val exprList: List<Expr>)
data class OpLink(val line: ExprLine)
data class OpCall(val expr: Expr)

// --- constructors

val Stack<Op>.expr get() = Expr(this)
fun Expr.plus(op: Op) = opStack.push(op).expr
fun Expr.plus(line: ExprLine) = plus(op(opLink(line)))
fun expr(vararg ops: Op) = stack(*ops).expr
fun expr(line: ExprLine, vararg lines: ExprLine) = expr().plus(line).fold(lines) { plus(it) }

fun op(argument: Argument): Op = ArgumentOp(argument)
fun op(access: IntAccess): Op = AccessOp(access)
fun op(link: OpLink): Op = LinkOp(link)
fun op(switch: OpSwitch): Op = SwitchOp(switch)
fun op(call: OpCall): Op = CallOp(call)

fun access(int: Int) = IntAccess(int)
fun opLink(exprLine: ExprLine) = OpLink(exprLine)
infix fun Int.lineTo(expr: Expr) = ExprLine(this, expr)

fun switchOp(vararg exprs: Expr) = op(OpSwitch(exprs.toList().reversed()))
fun call(expr: Expr) = OpCall(expr)

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
		is CallOp -> call.eval(bindings, lhs)
	}

fun Argument.eval(bindings: ValueBindings, lhs: Value) = bindings.stack.drop(previousStack)!!.top
fun IntAccess.eval(bindings: ValueBindings, lhs: Value) = lhs.access(int)
fun OpLink.eval(bindings: ValueBindings, lhs: Value) = lhs.plus(line.int lineTo line.rhs.eval(bindings))

fun OpSwitch.eval(bindings: ValueBindings, lhs: Value) =
	lhs.lastLine.let { line ->
		exprList[line.int].eval(bindings.push(line.rhs))
	}

fun OpCall.eval(bindings: ValueBindings, lhs: Value): Value =
	expr.eval(bindings.push(lhs))

// --- value -> expr

val Value.expr: Expr
	get() =
		lineStack.map { op }.expr

val ValueLine.op
	get() =
		op(opLink(int lineTo rhs.expr))

// --- type -> expr

val Type.exprOrNull: Expr?
	get() =
		onlyChoiceStackOrNull?.let { choiceStack ->
			expr().orNull.fold(choiceStack) { choice ->
				this?.run {
					choice.exprLineOrNull?.let(::plus)
				}
			}
		}

val Choice.exprLineOrNull: ExprLine?
	get() =
		onlyLineOrNull?.let { line ->
			line.rhs.exprOrNull?.let { rhsExpr ->
				0 lineTo rhsExpr
			}
		}
