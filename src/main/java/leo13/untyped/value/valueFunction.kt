package leo13.untyped.value

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expression.*
import leo13.untyped.functionName
import leo13.untyped.pattern.PatternArrow
import leo13.untyped.pattern.arrowTo
import leo13.untyped.pattern.pattern

data class ValueFunction(val given: ValuesGiven, val expression: Expression, val patternArrow: PatternArrow) {
	override fun toString() = scriptLine.toString()
}

fun function(
	valueGiven: ValuesGiven = givenValues(),
	expression: Expression = expression(),
	patternArrow: PatternArrow = pattern().arrowTo(pattern())) =
	ValueFunction(valueGiven, expression, patternArrow)

val ValueFunction.scriptLine: ScriptLine
	get() =
		functionName lineTo script(given.scriptLine, expression.scriptLine)

fun ValueFunction.apply(value: Value): Value =
	given.plus(value).evaluate(expression)