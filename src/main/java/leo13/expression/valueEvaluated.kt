package leo13.expression

import leo13.evaluatedName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.value.Value
import leo13.value.bodyScript

data class ValueEvaluated(val value: Value)

val Value.evaluated get() = ValueEvaluated(this)
fun evaluated(value: Value) = value.evaluated

val ValueEvaluated.scriptLine: ScriptLine
	get() =
		evaluatedName lineTo value.bodyScript
