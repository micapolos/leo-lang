package leo13.untyped.evaluator

import leo.base.orNullFold
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.*
import leo13.untyped.value.*
import leo13.untyped.value.Value
import leo9.Stack
import leo9.reverse
import leo9.seq

data class Evaluator(
	val parentOrNull: EvaluatorParent?,
	val context: Context,
	val value: Value): TokenReader {
	override fun toString() = scriptLine.toString()

	override fun begin(name: String) =
		when (name) {
			"get" -> getEvaluator
			setName -> setEvaluator
			else -> linkEvaluator(name)
		}

	override val end get() =
		parentOrNull?.plus(value)
}

fun EvaluatorParent?.evaluator(context: Context = context(), value: Value = leo13.untyped.value.value()) =
	Evaluator(this, context, value)

fun evaluator(context: Context = context(), value: Value = leo13.untyped.value.value()) =
	null.evaluator(context, value)

fun EvaluatorLink.evaluator(value: Value) =
	evaluator.plus(name lineTo value)

fun Evaluator.linkEvaluator(name: String) =
	linkTo(name).parent.evaluator(context)

fun Evaluator.plus(line: ValueLine): Evaluator =
	set(value.plus(line))

fun Evaluator.plusGet(name: String) =
	value.getOrNull(name)?.let { set(it) }

fun Evaluator.plusSet(line: ValueLine) =
	value.setOrNull(line)?.let { set(it) }

fun Evaluator.plusSet(lineStack: Stack<ValueLine>) =
	orNullFold(lineStack.reverse.seq) { plusSet(it) }

fun Evaluator.set(value: Value): Evaluator =
	copy(value = value)

val Evaluator.scriptLine get() =
	evaluatorName lineTo script(
		parentName lineTo script("todo"),
		contextName lineTo script("todo"),
		value.scriptLine)