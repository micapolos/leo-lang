package leo13.untyped.value

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expression.*
import leo13.untyped.functionName

data class Function(val bindings: Bindings, val expression: Expression) {
	override fun toString() = scriptLine.toString()
}

fun function(bindings: Bindings = bindings(), expression: Expression = expression()) =
	Function(bindings, expression)

val Function.scriptLine: ScriptLine
	get() =
		functionName lineTo script(bindings.scriptLine, expression.scriptLine)

fun Function.apply(value: Value): Value =
	bindings.plus(value).evaluate(expression)