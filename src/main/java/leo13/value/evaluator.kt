package leo13.value

import leo.base.notNullIf
import leo13.Lhs
import leo13.Rhs
import leo13.RhsLine
import leo13.Wrap
import leo13.script.Scriptable
import leo9.drop
import leo9.linkOrNull
import leo9.mapFirst

data class Evaluator(val bindings: ValueBindings, val value: Value) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "evaluator"
	override val scriptableBody get() = leo13.script.script(bindings.scriptableLine, value.scriptableLine)
}

fun evaluator() = Evaluator(valueBindings(), value())
fun evaluator(valueBindings: ValueBindings, value: Value) = Evaluator(valueBindings, value)

fun Evaluator.push(expr: Expr): Evaluator =
	when (expr) {
		is EmptyExpr -> this
		is ValueExpr -> push(expr.value)
		is GivenExpr -> push(expr.given)
		is LinkExpr -> push(expr.link)
	}

fun Evaluator.push(value: Value): Evaluator =
	evaluator(bindings, value)

fun Evaluator.push(given: Given): Evaluator =
	evaluator(
		bindings,
		bindings.stack.drop(given.previousStack)!!.linkOrNull!!.value)

fun Evaluator.push(link: ExprLink): Evaluator =
	push(link.lhs).push(link.op)

fun Evaluator.push(op: Op): Evaluator =
	evaluator(bindings, evaluate(op))

fun Evaluator.evaluate(expr: Expr): Value =
	push(expr).value

fun Evaluator.evaluate(op: Op): Value =
	when (op) {
		is LhsOp -> evaluate(op.lhs)
		is RhsLineOp -> evaluate(op.rhsLine)
		is RhsOp -> evaluate(op.rhs)
		is GetOp -> evaluate(op.get)
		is WrapOp -> evaluate(op.wrap)
		is SwitchOp -> evaluate(op.switch)
		is LineOp -> evaluate(op.line)
		is CallOp -> evaluate(op.call)
	}

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
			evaluator(bindings, line.rhs).evaluate(case.expr)
		}
	}

fun Evaluator.evaluate(line: ExprLine): Value =
	value.plus(line.name lineTo bindings.evaluate(line.rhs))

fun Evaluator.evaluate(call: Call): Value =
		value
			.fnOrNull!!
			.let { fn -> fn.valueBindings.push(bindings.evaluate(call.expr)).evaluate(fn.expr) }

val Expr.evaluate: Value
	get() =
		evaluator().push(this).value