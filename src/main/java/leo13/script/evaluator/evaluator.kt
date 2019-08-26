package leo13.script.evaluator

import leo.base.notNullIf
import leo13.*
import leo13.Argument
import leo13.Lhs
import leo13.Rhs
import leo13.script.*
import leo13.script.Case
import leo13.script.Switch
import leo9.*

data class Evaluator(val bindings: Bindings, val value: Value) {
	override fun toString() = asScriptLine.toString()
	val asScriptLine
		get() = "evaluator" lineTo script(bindings.asScriptLine, value.asScriptLine)
}

fun evaluator() = Evaluator(bindings(), value())
fun evaluator(bindings: Bindings, value: Value) = Evaluator(bindings, value)
fun Evaluator.put(bindings: Bindings) = copy(bindings = bindings)
fun Evaluator.put(value: Value) = copy(value = value)

fun Evaluator.bind(value: Value) = put(bindings.push(value))

val Evaluator.begin get() = put(value())

fun Expr.evaluate(bindings: Bindings) = evaluator().put(bindings).push(this).value
val Expr.evaluate get() = evaluate(bindings())
fun Evaluator.evaluate(expr: Expr): Value = push(expr).value

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
	put(value.linkOrNull!!.lhs)

fun Evaluator.push(rhsLine: RhsLine): Evaluator =
	put(value(value.linkOrNull!!.line))

fun Evaluator.push(rhs: Rhs): Evaluator =
	put(value.linkOrNull!!.line.rhs)

fun Evaluator.push(get: Get): Evaluator =
	put(value.accessOrNull(get.name)!!)

fun Evaluator.push(switch: Switch): Evaluator =
	switch.caseStack.mapFirst { pushOrNull(this) }!!

fun Evaluator.pushOrNull(case: Case): Evaluator? =
	value.linkOrNull!!.line.let { line ->
		notNullIf(line.name == case.name) {
			put(line.rhs).push(case.expr)
		}
	}

fun Evaluator.push(line: ExprLine): Evaluator =
	put(value.plus(line.name lineTo begin.push(line.rhs).value))

fun Evaluator.push(call: Call): Evaluator =
	put(
		value
			.onlyFnOrNull!!
			.let { fn -> fn.expr.evaluate(fn.bindings.push(call.expr.evaluate(bindings))) })