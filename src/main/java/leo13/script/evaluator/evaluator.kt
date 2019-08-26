package leo13.script.evaluator

import leo.base.notNullIf
import leo13.*
import leo13.Lhs
import leo13.Rhs
import leo13.script.*
import leo13.script.Case
import leo13.script.Switch
import leo9.*

data class Evaluator(val bindings: Bindings, val value: Value) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "evaluator"
	override val scriptableBody get() = script(bindings.asScriptLine, value.scriptableLine)
}

fun evaluator() = Evaluator(bindings(), value())
fun evaluator(bindings: Bindings, value: Value) = Evaluator(bindings, value)
fun Evaluator.put(bindings: Bindings) = copy(bindings = bindings)
fun Evaluator.put(value: Value) = copy(value = value)

fun Evaluator.bind(value: Value) = put(bindings.push(value))

val Evaluator.begin get() = put(value())

fun Expr.evaluate(bindings: Bindings) = evaluator().put(bindings).push(this).value
val Expr.evaluate get() = evaluate(bindings())

fun Evaluator.push(expr: Expr) =
	fold(expr.opStack.reverse) { push(it) }

fun Evaluator.push(op: Op): Evaluator =
	put(evaluate(op))

fun Evaluator.evaluate(expr: Expr): Value =
	push(expr).value

fun Evaluator.evaluate(op: Op): Value =
	when (op) {
		is ArgumentOp -> evaluate(op.given)
		is LhsOp -> evaluate(op.lhs)
		is RhsLineOp -> evaluate(op.rhsLine)
		is RhsOp -> evaluate(op.rhs)
		is GetOp -> evaluate(op.get)
		is SwitchOp -> evaluate(op.switch)
		is LineOp -> evaluate(op.line)
		is CallOp -> evaluate(op.call)
	}

fun Evaluator.evaluate(given: Given): Value =
	bindings.stack.drop(given.previousStack)!!.linkOrNull!!.value

fun Evaluator.evaluate(lhs: Lhs): Value =
	value.linkOrNull!!.lhs

fun Evaluator.evaluate(rhsLine: RhsLine): Value =
	value(value.linkOrNull!!.line)

fun Evaluator.evaluate(rhs: Rhs): Value =
	value.linkOrNull!!.line.rhs

fun Evaluator.evaluate(get: Get): Value =
	value.accessOrNull(get.name)!!

fun Evaluator.evaluate(switch: Switch): Value =
	switch.caseStack.mapFirst { evaluateOrNull(this) }!!

fun Evaluator.evaluateOrNull(case: Case): Value? =
	value.linkOrNull!!.line.let { line ->
		notNullIf(line.name == case.name) {
			put(line.rhs).evaluate(case.expr)
		}
	}

fun Evaluator.evaluate(line: ExprLine): Value =
	value.plus(line.name lineTo begin.evaluate(line.rhs))

fun Evaluator.evaluate(call: Call): Value =
		value
			.fnOrNull!!
			.let { fn -> fn.expr.evaluate(fn.bindings.push(call.expr.evaluate(bindings))) }