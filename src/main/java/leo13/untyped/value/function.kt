package leo13.untyped.value

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expression.*
import leo13.untyped.functionName

data class Function(val bindings: Bindings, val expression: Expression) {
	override fun toString() = scriptLine.toString()
}

val Function.scriptLine
	get() =
		functionName lineTo script(bindings.scriptLine, expression.scriptLine)

fun Function.apply(value: Value): Value =
	bindings.plus(value).evaluate(expression)