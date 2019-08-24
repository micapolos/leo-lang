package leo13.script.evaluator

import leo.base.notNullIf
import leo13.*
import leo13.Argument
import leo13.Lhs
import leo13.Rhs
import leo13.Script
import leo13.script
import leo13.script.*
import leo13.script.Case
import leo9.*

data class Evaluator(val bindings: Bindings, val script: Script) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine
		get() = "evaluator" lineTo script(bindings.asScriptLine, script.asScriptLine)
}

fun evaluator() = Evaluator(bindings(), script())
fun Evaluator.put(bindings: Bindings) = copy(bindings = bindings)
fun Evaluator.put(script: Script) = copy(script = script)

fun Evaluator.bind(script: Script) = put(bindings.push(script))

val Evaluator.begin get() = put(script())

fun Expr.evaluate(bindings: Bindings) = evaluator().put(bindings).push(this).script
val Expr.evaluate get() = evaluate(bindings())

fun Evaluator.push(expr: Expr) =
	fold(expr.opStack.reverse) { push(it) }

fun Evaluator.push(op: Op): Evaluator =
	when (op) {
		is ArgumentOp -> push(op.argument)
		is LhsOp -> push(op.lhs)
		is RhsLineOp -> push(op.rhsLine)
		is RhsOp -> push(op.rhs)
		is GetOp -> push(op.get)
		is SwitchOp -> push(op.switch)
		is LineOp -> push(op.line)
		is CallOp -> push(op.call)
	}

fun Evaluator.push(argument: Argument): Evaluator =
	put(bindings.stack.drop(argument.previousStack)!!.linkOrNull!!.value)

fun Evaluator.push(lhs: Lhs): Evaluator =
	put(script.linkOrNull!!.lhs)

fun Evaluator.push(rhsLine: RhsLine): Evaluator =
	put(script.linkOrNull!!.line.onlyStack.script)

fun Evaluator.push(rhs: Rhs): Evaluator =
	put(script.linkOrNull!!.line.rhs)

fun Evaluator.push(get: Get): Evaluator =
	put(script.accessOrNull(get.name)!!)

fun Evaluator.push(switch: Switch): Evaluator =
	switch.caseStack.mapFirst { pushOrNull(this) }!!

fun Evaluator.pushOrNull(case: Case): Evaluator? =
	script.linkOrNull!!.line.let { line ->
		notNullIf(line.name == case.name) {
			put(line.rhs).push(case.expr)
		}
	}

fun Evaluator.push(line: ExprLine): Evaluator =
	put(script.plus(line.name lineTo begin.push(line.rhs).script))

fun Evaluator.push(call: Call): Evaluator =
	bind(script).push(call.expr)
