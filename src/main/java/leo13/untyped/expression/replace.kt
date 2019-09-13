package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.forgetName
import leo13.untyped.replaceName
import leo13.untyped.value.Value
import leo13.untyped.value.bodyScript
import leo13.untyped.value.isEmpty
import leo13.untyped.value.value

data class Replace(val value: Value)

fun replace(value: Value) = Replace(value)
fun forget() = replace(value())

val Replace.scriptLine
	get() =
		if (value.isEmpty) forgetName lineTo script()
		else replaceName lineTo value.bodyScript