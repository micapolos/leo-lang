package leo13.untyped.value

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expression.*
import leo13.untyped.functionName

data class ValueFunction(val given: ValueGiven, val expression: Expression) {
	override fun toString() = scriptLine.toString()
}

fun function(
	valueGiven: ValueGiven = given(value()),
	expression: Expression = expression()) =
	ValueFunction(valueGiven, expression)

val ValueFunction.scriptLine: ScriptLine
	get() =
		functionName lineTo script(given.scriptLine, expression.scriptLine)

fun ValueFunction.apply(value: Value): Value =
	given.plus(value).evaluate(expression)