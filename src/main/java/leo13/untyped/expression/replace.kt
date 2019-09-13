package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.constantName
import leo13.untyped.value.Value

data class Replace(val value: Value)

fun constant(value: Value) = Replace(value)

val Replace.scriptLine
	get() =
		constantName lineTo script()