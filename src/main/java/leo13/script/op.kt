package leo13.script

import leo13.*
import leo9.drop
import leo9.linkOrNull
import leo9.onlyStack

sealed class Op

data class ArgumentOp(val argument: Argument) : Op()
data class LhsOp(val lhs: Lhs) : Op()
data class RhsLineOp(val rhsLine: RhsLine) : Op()
data class RhsOp(val rhs: Rhs) : Op()
data class NameOp(val name: String) : Op()
data class SwitchOp(val switch: Switch) : Op()
data class LineOp(val line: ExprLine) : Op()
data class CallOp(val call: Call) : Op()

fun op(argument: Argument): Op = ArgumentOp(argument)
fun op(lhs: Lhs): Op = LhsOp(lhs)
fun op(rhs: Rhs): Op = RhsOp(rhs)
fun op(rhsLine: RhsLine): Op = RhsLineOp(rhsLine)
fun op(name: String): Op = NameOp(name)
fun op(switch: Switch): Op = SwitchOp(switch)
fun op(line: ExprLine): Op = LineOp(line)
fun op(call: Call): Op = CallOp(call)

val Op.lineOrNull get() = (this as? LineOp)?.line

fun Op.eval(bindings: Bindings, script: Script): Script =
	when (this) {
		is ArgumentOp -> argument.eval(bindings, script)
		is LhsOp -> lhs.eval(bindings, script)
		is RhsLineOp -> rhsLine.eval(bindings, script)
		is RhsOp -> rhs.eval(bindings, script)
		is NameOp -> name.eval(bindings, script)
		is SwitchOp -> switch.eval(bindings, script)
		is LineOp -> line.eval(bindings, script)
		is CallOp -> call.eval(bindings, script)
	}

fun Argument.eval(bindings: Bindings, script: Script): Script =
	bindings.stack.drop(previousStack)!!.linkOrNull!!.value

fun Lhs.eval(bindings: Bindings, script: Script): Script =
	script.linkOrNull!!.lhs

fun RhsLine.eval(bindings: Bindings, script: Script): Script =
	script.linkOrNull!!.line.onlyStack.script

fun Rhs.eval(bindings: Bindings, script: Script): Script =
	script.linkOrNull!!.line.rhs

fun String.eval(bindings: Bindings, script: Script): Script =
	script.accessOrNull(this)!!

fun ExprLine.eval(bindings: Bindings, script: Script): Script =
	script.plus(name lineTo rhs.eval(bindings))
