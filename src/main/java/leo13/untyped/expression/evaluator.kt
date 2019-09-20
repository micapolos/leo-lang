package leo13.untyped.expression

import leo.base.notNullIf
import leo13.fold
import leo13.mapFirst
import leo13.reverse
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.evaluatorName
import leo13.untyped.value.*

data class Evaluator(val context: ValueContext, val evaluated: ValueEvaluated) {
	override fun toString() = scriptLine.toString()
}

fun ValueContext.evaluator(evaluated: ValueEvaluated = evaluated(value())) =
	Evaluator(this, evaluated)

fun evaluator(evaluated: ValueEvaluated = evaluated(value())) =
	valueContext().give(value()).evaluator(evaluated)

fun Evaluator.set(evaluated: ValueEvaluated) = context.evaluator(evaluated)

fun Evaluator.plus(expression: Expression): Evaluator =
	fold(expression.opStack.reverse) { plus(it) }

fun Evaluator.plus(op: Op): Evaluator =
	when (op) {
		is ValueOp -> plus(op.value)
		is WrapOp -> plus(op.wrap)
		is PlusOp -> plus(op.plus)
		is GetOp -> plus(op.get)
		is SetOp -> plus(op.set)
		is PreviousOp -> plus(op.previous)
		is ContentOp -> plus(op.content)
		is SwitchOp -> plus(op.switch)
		is SwitchedOp -> plus(op.switched)
		is GiveOp -> plus(op.give)
		is GivenOp -> plus(op.given)
		is ApplyOp -> plus(op.apply)
	}

fun Evaluator.plus(value: Value): Evaluator =
	set(evaluated(evaluated.value.plus(value)))

fun Evaluator.plus(wrap: Wrap): Evaluator =
	set(evaluated(value(item(wrap.name lineTo evaluated.value))))

fun Evaluator.plus(get: Get): Evaluator =
	set(evaluated.value.getOrNull(get.name)!!.evaluated)

fun Evaluator.plus(plus: Plus): Evaluator =
	set(evaluated.value.plus(context.evaluate(plus.line)).evaluated)

fun Evaluator.plus(set: Set): Evaluator =
	set(evaluated.value.setOrNull(context.evaluate(set.line))!!.evaluated)

fun Evaluator.plus(previous: Previous): Evaluator =
	set(evaluated.value.previousOrNull!!.evaluated)

fun Evaluator.plus(content: Content): Evaluator =
	set(evaluated.value.firstItemOrNull!!.lineOrNull!!.rhs.evaluated)

fun Evaluator.plus(switch: Switch): Evaluator =
	evaluated.value.linkOrNull!!.let { valueLink ->
		valueLink.rhsItem.lineOrNull!!.let { line ->
			line.rhs.linkOrNull?.rhsItem?.lineOrNull!!.let { caseLine ->
				set(valueLink.lhsValue.evaluated).plus(switch, caseLine)
			}
		}
	}

fun Evaluator.plus(switch: Switch, line: ValueLine): Evaluator =
	switch.caseStack.mapFirst { plusOrNull(this, line) }!!

fun Evaluator.plusOrNull(case: Case, line: ValueLine): Evaluator? =
	notNullIf(line.name == case.name) {
		plus(context.switch(value(item(line))).evaluate(case.expression))
	}

fun Evaluator.plus(given: Given): Evaluator =
	set(evaluated.value.plus(context.given.value).evaluated)

fun Evaluator.plus(switched: Switched): Evaluator =
	set(context.switched.value.evaluated)

fun Evaluator.plus(give: Give): Evaluator =
	set(context.give(evaluated.value).evaluate(give.expression).evaluated)

fun Evaluator.plus(apply: Apply): Evaluator =
	evaluated.value.linkOrNull!!.let { link ->
		set(
			link
				.lhsValue
				.plus(
					link
						.rhsItem
						.lineOrNull!!
						.rhs
						.firstItemOrNull!!
						.functionOrNull!!
						.apply(context.evaluate(apply.expression))).evaluated)
	}

val Evaluator.scriptLine
	get() =
		evaluatorName lineTo script(context.scriptingLine, evaluated.scriptLine)