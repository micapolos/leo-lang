package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.constantName
import leo13.untyped.value.Value

data class Constant(val value: Value)

fun constant(value: Value) = Constant(value)

val Constant.scriptLine
	get() =
		constantName lineTo script()