package leo13.script

import leo13.Argument
import leo13.Lhs
import leo13.Rhs
import leo13.RhsLine

sealed class Op

data class ArgumentOp(val argument: Argument) : Op()
data class LhsOp(val lhs: Lhs) : Op()
data class RhsLineOp(val rhsLine: RhsLine) : Op()
data class RhsOp(val rhs: Rhs) : Op()
data class GetOp(val get: Get) : Op()
data class SwitchOp(val switch: Switch) : Op()
data class LineOp(val line: ExprLine) : Op()
data class CallOp(val call: Call) : Op()

fun op(argument: Argument): Op = ArgumentOp(argument)
fun op(lhs: Lhs): Op = LhsOp(lhs)
fun op(rhs: Rhs): Op = RhsOp(rhs)
fun op(rhsLine: RhsLine): Op = RhsLineOp(rhsLine)
fun op(get: Get): Op = GetOp(get)
fun op(switch: Switch): Op = SwitchOp(switch)
fun op(line: ExprLine): Op = LineOp(line)
fun op(call: Call): Op = CallOp(call)

val Op.lineOrNull get() = (this as? LineOp)?.line
