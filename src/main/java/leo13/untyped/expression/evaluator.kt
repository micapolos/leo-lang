package leo13.untyped.expression

import leo.base.notNullIf
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.evaluatorName
import leo13.untyped.value.*
import leo9.*

data class Evaluator(val bindings: Bindings, val value: Value) {
	override fun toString() = scriptLine.toString()
}

fun Bindings.evaluator(value: Value = value()) = Evaluator(this, value)

fun evaluator(value: Value = value()) = bindings().evaluator(value)

fun Evaluator.set(value: Value) = bindings.evaluator(value)

fun Evaluator.plus(expression: Expression): Evaluator =
	fold(expression.opStack.reverse) { plus(it) }

fun Evaluator.plus(op: Op): Evaluator =
	when (op) {
		is ReplacetOp -> plus(op.replace)
		is PlusOp -> plus(op.plus)
		is GetOp -> plus(op.get)
		is SetOp -> plus(op.set)
		is PreviousOp -> plus(op.previous)
		is SwitchOp -> plus(op.switch)
		is BindOp -> plus(op.bind)
		is BoundOp -> plus(op.bound)
		is ApplyOp -> plus(op.apply)
	}

fun Evaluator.plus(replace: Replace): Evaluator =
	set(replace.value)

fun Evaluator.plus(get: Get): Evaluator =
	set(value.getOrNull(get.name)!!)

fun Evaluator.plus(plus: Plus): Evaluator =
	set(value.plus(bindings.evaluate(plus.line)))

fun Evaluator.plus(set: Set): Evaluator =
	set(value.setOrNull(bindings.evaluate(set.line))!!)

fun Evaluator.plus(previous: Previous): Evaluator =
	set(value.previousOrNull!!)

fun Evaluator.plus(switch: Switch): Evaluator =
	switch.caseStack.mapFirst { plusOrNull(this) }!!

fun Evaluator.plusOrNull(case: Case): Evaluator? =
	when (value.itemStack) {
		is EmptyStack -> null
		is LinkStack -> value.itemStack.link.value.lineOrNull?.let { line ->
			notNullIf(line.name == case.name) {
				plus(case.expression)
			}
		}
	}

fun Evaluator.plus(bound: Bound): Evaluator =
	set(bindings.valueOrNull(bound)!!)

fun Evaluator.plus(bind: Bind): Evaluator =
	set(bindings.plus(value).evaluate(bind.expression))

fun Evaluator.plus(apply: Apply): Evaluator =
	set(value.firstItemOrNull?.functionOrNull!!.apply(bindings.evaluate(apply.expression)))

val Evaluator.scriptLine
	get() =
		evaluatorName lineTo script(bindings.scriptLine, value.scriptLine)