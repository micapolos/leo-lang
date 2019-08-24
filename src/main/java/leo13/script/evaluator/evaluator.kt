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

data class Evaluator(val bindings: Bindings, val evaluated: Evaluated) {
	override fun toString() = asScript.toString()
	val asScript
		get() = script(
			"bindings" lineTo bindings.asScript,
			"evaluated" lineTo evaluated.asScript)
}

val leo13.Evaluator.begin get() = Evaluator(bindings(), evaluated(script()))
fun Evaluator.bind(script: Script) = copy(bindings = bindings.push(script))
fun Evaluator.put(evaluated: Evaluated) = copy(evaluated = evaluated)

val Evaluator.begin get() = put(evaluated(script()))
val Evaluator.end get() = evaluated.script

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
	put(evaluated(bindings.stack.drop(argument.previousStack)!!.linkOrNull!!.value))

fun Evaluator.push(lhs: Lhs): Evaluator =
	put(evaluated(evaluated.script.linkOrNull!!.lhs))

fun Evaluator.push(rhsLine: RhsLine): Evaluator =
	put(evaluated(evaluated.script.linkOrNull!!.line.onlyStack.script))

fun Evaluator.push(rhs: Rhs): Evaluator =
	put(evaluated(evaluated.script.linkOrNull!!.line.rhs))

fun Evaluator.push(get: Get): Evaluator =
	put(evaluated(evaluated.script.accessOrNull(get.name)!!))

fun Evaluator.push(switch: Switch): Evaluator =
	switch.caseStack.mapFirst { pushOrNull(this) }!!

fun Evaluator.pushOrNull(case: Case): Evaluator? =
	evaluated.script.linkOrNull!!.line.let { line ->
		notNullIf(line.name == case.name) {
			put(evaluated(line.rhs)).push(case.expr)
		}
	}

fun Evaluator.push(line: ExprLine): Evaluator =
	put(evaluated(evaluated.script.plus(line.name lineTo begin.push(line.rhs).end)))

fun Evaluator.push(call: Call): Evaluator =
	bind(evaluated.script).push(call.expr)
