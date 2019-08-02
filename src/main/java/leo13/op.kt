package leo13

import leo.base.Empty

sealed class Op
data class GetOp(val get: OpGet) : Op()
data class LinkOp(val link: OpLink) : Op()
data class SwitchOp(val switch: OpSwitch) : Op()
data class CallOp(val call: OpCall) : Op()

data class OpGet(val int: Int)
data class OpCall(val parameterExpr: Expr)

sealed class OpSwitch
data class EmptyOpSwitch(val empty: Empty) : OpSwitch()
data class LinkOpSwitch(val link: SwitchLink) : OpSwitch()
data class SwitchLink(val lhs: OpSwitch, val rhs: Expr)

data class OpLink(val line: ExprLine)

fun op(get: OpGet): Op = GetOp(get)
fun op(link: OpLink): Op = LinkOp(link)

fun get(int: Int) = OpGet(int)
fun opLink(exprLine: ExprLine) = OpLink(exprLine)

// --- eval

fun Op.eval(parameter: Value, lhs: Value): Value =
	when (this) {
		is GetOp -> get.eval(parameter, lhs)
		is LinkOp -> link.eval(parameter, lhs)
		is SwitchOp -> TODO()
		is CallOp -> TODO()
	}

fun OpGet.eval(parameter: Value, lhs: Value) = lhs.get(int)
fun OpLink.eval(parameter: Value, lhs: Value) = value(link(lhs, line.eval(parameter)))