package leo22.term

import leo22.dsl.*

val valueDef = value(choice(native(), function()))

fun X.valueApply(value: X): X =
	function.functionApply(value)