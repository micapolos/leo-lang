package leo13.untyped.value

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expression.*
import leo13.untyped.functionName

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