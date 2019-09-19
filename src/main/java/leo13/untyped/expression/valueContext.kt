package leo13.untyped.expression

import leo13.ObjectScripting
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.value.Value
import leo13.untyped.value.ValueLine
import leo13.untyped.value.lineTo
import leo13.untyped.value.value

data class ValueContext(
	val given: ValueGiven,
	val switched: ValueSwitched) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = "context" lineTo script(
			given.scriptLine,
			switched.scriptingLine)

	fun give(value: Value) =
		copy(given = given.plus(value))

	fun switch(value: Value) =
		copy(switched = switched.plus(value))

	fun evaluate(expression: Expression): Value =
		evaluator().plus(expression).evaluated.value

	fun evaluate(line: ExpressionLine): ValueLine =
		line.name lineTo evaluate(line.rhs)
}

fun valueContext() = ValueContext(given(value()), switched(value()))
