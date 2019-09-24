package leo13.expression

import leo.base.notNullIf
import leo13.ObjectScripting
import leo13.contextName
import leo13.mapFirst
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.value.*

data class ValueContext(
	val given: ValueGiven,
	val matching: ValueMatching) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = contextName lineTo script(
			given.scriptLine,
			matching.scriptingLine)

	fun give(value: Value) =
		copy(given = given.plus(value))

	fun switch(value: Value) =
		copy(matching = matching.plus(value))

	fun evaluate(expression: Expression): Value =
		evaluator().plus(expression).evaluated.value

	fun evaluate(line: ExpressionLine): ValueLine =
		line.name lineTo evaluate(line.rhs)

	fun evaluate(switch: Switch, line: ValueLine): Value =
		switch.caseStack.mapFirst { evaluateOrNull(this, line) }!!

	fun evaluateOrNull(case: Case, line: ValueLine): Value? =
		notNullIf(line.name == case.name) {
			switch(value(item(line))).evaluate(case.expression)
		}
}

fun valueContext() = ValueContext(given(value()), matching(value()))
