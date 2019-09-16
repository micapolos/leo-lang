package leo13.untyped.expression

import leo.base.notNullIf
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.evaluatorName
import leo13.untyped.value.*
import leo9.fold
import leo9.mapFirst
import leo9.reverse

data class Evaluator(val given: ValueGiven, val evaluated: ValueEvaluated) {
	override fun toString() = scriptLine.toString()
}

fun ValueGiven.evaluator(evaluated: ValueEvaluated = evaluated(value())) =
	Evaluator(this, evaluated)

fun evaluator(evaluated: ValueEvaluated = evaluated(value())) =
	value().given.evaluator(evaluated)

fun Evaluator.set(evaluated: ValueEvaluated) = given.evaluator(evaluated)

fun Evaluator.plus(expression: Expression): Evaluator =
	fold(expression.opStack.reverse) { plus(it) }

fun Evaluator.plus(op: Op): Evaluator =
	when (op) {
		is ValueItemOp -> plus(op.item)
		is PlusOp -> plus(op.plus)
		is GetOp -> plus(op.get)
		is SetOp -> plus(op.set)
		is PreviousOp -> plus(op.previous)
		is ContentOp -> plus(op.content)
		is SwitchOp -> plus(op.switch)
		is GiveOp -> plus(op.give)
		is GivenOp -> plus(op.given)
		is ApplyOp -> plus(op.apply)
	}

fun Evaluator.plus(item: ValueItem): Evaluator =
	set(evaluated.value.plus(item).evaluated)

fun Evaluator.plus(get: Get): Evaluator =
	set(evaluated.value.getOrNull(get.name)!!.evaluated)

fun Evaluator.plus(plus: Plus): Evaluator =
	set(evaluated.value.plus(given.evaluate(plus.line)).evaluated)

fun Evaluator.plus(set: Set): Evaluator =
	set(evaluated.value.setOrNull(given.evaluate(set.line))!!.evaluated)

fun Evaluator.plus(previous: Previous): Evaluator =
	set(evaluated.value.previousOrNull!!.evaluated)

fun Evaluator.plus(content: Content): Evaluator =
	set(evaluated.value.firstItemOrNull!!.lineOrNull!!.rhs.evaluated)

fun Evaluator.plus(switch: Switch): Evaluator =
	evaluated.value.linkOrNull!!.let { valueLink ->
		valueLink.rhsItem.lineOrNull!!.let { line ->
			line.rhs.onlyLineOrNull!!.let { caseLine ->
				given
					.evaluator(evaluated(valueLink.lhsValue))
					.plus(caseLine, switch)
			}
		}
	}

fun Evaluator.plus(line: ValueLine, switch: Switch): Evaluator =
	switch.caseStack.mapFirst { plusOrNull(line, this) }!!

fun Evaluator.plusOrNull(line: ValueLine, case: Case): Evaluator? =
	notNullIf(line.name == case.name) {
		plus(line.rhs).plus(case.expression)
	}

fun Evaluator.plus(given: Given): Evaluator =
	set(this.given.value.evaluated)

fun Evaluator.plus(give: Give): Evaluator =
	set(given.plus(evaluated.value).evaluate(give.expression).evaluated)

fun Evaluator.plus(apply: Apply): Evaluator =
	set(evaluated.value.firstItemOrNull?.functionOrNull!!.apply(given.evaluate(apply.expression)).evaluated)

fun Evaluator.plus(value: Value): Evaluator =
	set(evaluated(evaluated.value.plus(value)))

val Evaluator.scriptLine
	get() =
		evaluatorName lineTo script(given.scriptLine, evaluated.scriptLine)