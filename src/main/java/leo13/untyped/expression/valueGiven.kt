package leo13.untyped.expression

import leo13.script.ScriptLine
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.untyped.givenName
import leo13.untyped.value.*

data class ValueGiven(val value: Value)

val Value.given get() = ValueGiven(this)
fun given(value: Value) = value.given

fun ValueGiven.plus(newValue: Value) =
	value.plus(givenName lineTo newValue).given

fun ValueGiven.evaluate(expression: Expression): Value =
	evaluator().plus(expression).evaluated.value

fun ValueGiven.evaluate(line: ExpressionLine): ValueLine =
	line.name lineTo evaluate(line.rhs)

val ValueGiven.scriptLine: ScriptLine
	get() =
		"context" lineTo value.bodyScript.emptyIfEmpty
