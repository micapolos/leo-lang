package leo13.expression

import leo13.givenName
import leo13.script.ScriptLine
import leo13.script.emptyIfEmpty
import leo13.script.lineTo
import leo13.value.Value
import leo13.value.bodyScript
import leo13.value.lineTo
import leo13.value.plus

data class ValueGiven(val value: Value)

val Value.given get() = ValueGiven(this)
fun given(value: Value) = value.given

fun ValueGiven.plus(newValue: Value) =
	value.plus(givenName lineTo newValue).given

val ValueGiven.scriptLine: ScriptLine
	get() =
		"context" lineTo value.bodyScript.emptyIfEmpty
