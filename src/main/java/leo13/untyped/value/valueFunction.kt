package leo13.untyped.value

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.PatternArrow
import leo13.untyped.arrowTo
import leo13.untyped.expression.*
import leo13.untyped.functionName
import leo13.untyped.pattern

data class ValueFunction(val bindings: Bindings, val expression: Expression, val patternArrow: PatternArrow) {
	override fun toString() = scriptLine.toString()
}

fun function(
	bindings: Bindings = bindings(),
	expression: Expression = expression(),
	patternArrow: PatternArrow = pattern().arrowTo(pattern())) =
	ValueFunction(bindings, expression, patternArrow)

val ValueFunction.scriptLine: ScriptLine
	get() =
		functionName lineTo script(bindings.scriptLine, expression.scriptLine)

fun ValueFunction.apply(value: Value): Value =
	bindings.plus(value).evaluate(expression)