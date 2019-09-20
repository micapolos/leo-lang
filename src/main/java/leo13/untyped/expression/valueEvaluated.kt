package leo13.untyped.expression

import leo13.evaluatedName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.untyped.value.Value
import leo13.untyped.value.bodyScript

data class ValueEvaluated(val value: Value)

val Value.evaluated get() = ValueEvaluated(this)
fun evaluated(value: Value) = value.evaluated

val ValueEvaluated.scriptLine: ScriptLine
	get() =
		evaluatedName lineTo value.bodyScript
