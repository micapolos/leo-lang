package leo25

import leo14.Script

data class Function(val context: Context, val script: Script)

fun Function.apply(value: Value): Value =
	context
		.plusGiven(value)
		.interpretedValueOrNull(script)
		?: value("nothing" to null)
