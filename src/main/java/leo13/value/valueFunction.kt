package leo13.value

import leo13.expression.*
import leo13.functionName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class ValueFunction(val context: ValueContext, val expression: Expression) {
	override fun toString() = scriptLine.toString()
}

fun function(
	context: ValueContext = valueContext(),
	expression: Expression = expression()) =
	ValueFunction(context, expression)

val ValueFunction.scriptLine: ScriptLine
	get() =
		functionName lineTo script(context.scriptingLine, expression.scriptLine)

fun ValueFunction.apply(value: Value): Value =
	context.give(value).evaluate(expression)

fun ValueFunction.fix(value: Value): Value =
	context.give(value(item(this))).give(value).evaluate(expression)
