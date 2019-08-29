package leo13.value

import leo.base.notNullIf
import leo13.Lhs
import leo13.Rhs
import leo13.RhsLine
import leo13.Wrap
import leo13.script.Scriptable
import leo9.*

data class Evaluator(val valueBindings: ValueBindings, val value: Value) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "evaluator"
	override val scriptableBody get() = leo13.script.script(valueBindings.scriptableLine, value.scriptableLine)
}

fun evaluator() = Evaluator(valueBindings(), value())
fun evaluator(valueBindings: ValueBindings, value: Value) = Evaluator(valueBindings, value)

fun Evaluator.push(expr: Expr) =
	fold(expr.opStack.reverse) { push(it) }

fun Evaluator.push(op: Op): Evaluator =
	evaluator(valueBindings, evaluate(op))

fun Evaluator.evaluate(expr: Expr): Value =
	push(expr).value

fun Evaluator.evaluate(op: Op): Value =
	when (op) {
		is ValueOp -> evaluate(op.value)
		is ArgumentOp -> evaluate(op.given)
		is LhsOp -> evaluate(op.lhs)
		is RhsLineOp -> evaluate(op.rhsLine)
		is RhsOp -> evaluate(op.rhs)
		is GetOp -> evaluate(op.get)
		is WrapOp -> evaluate(op.wrap)
		is SwitchOp -> evaluate(op.switch)
		is LineOp -> evaluate(op.line)
		is CallOp -> evaluate(op.call)
	}

fun Evaluator.evaluate(value: Value): Value =
	value

fun Evaluator.evaluate(given: Given): Value =
	valueBindings.stack.drop(given.previousStack)!!.linkOrNull!!.value

fun Evaluator.evaluate(lhs: Lhs): Value =
	value.linkOrNull!!.lhs

fun Evaluator.evaluate(rhsLine: RhsLine): Value =
	value(value.linkOrNull!!.line)

fun Evaluator.evaluate(rhs: Rhs): Value =
	value.linkOrNull!!.line.rhs

fun Evaluator.evaluate(get: Get): Value =
	value.accessOrNull(get.name)!!

fun Evaluator.evaluate(wrap: Wrap): Value =
	value.wrapOrNull!!

fun Evaluator.evaluate(switch: Switch): Value =
	switch.caseStack.mapFirst { evaluateOrNull(this) }!!

fun Evaluator.evaluateOrNull(case: Case): Value? =
	value.linkOrNull!!.line.let { line ->
		notNullIf(line.name == case.name) {
			evaluator(valueBindings, line.rhs).evaluate(case.expr)
		}
	}

fun Evaluator.evaluate(line: ExprLine): Value =
	value.plus(line.name lineTo valueBindings.evaluate(line.rhs))

fun Evaluator.evaluate(call: Call): Value =
		value
			.fnOrNull!!
			.let { fn -> fn.valueBindings.push(valueBindings.evaluate(call.expr)).evaluate(fn.expr) }

val Expr.evaluate: Value
	get() =
		evaluator().evaluate(this)