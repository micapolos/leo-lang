package leo14

import leo14.native.*

val Native.scriptLine: ScriptLine
	get() =
		when (this) {
			is BooleanNative -> "$boolean" lineTo script()
			is StringNative -> line(literal(string))
			is IntNative -> line(literal(int))
			is DoubleNative -> line(literal(double))
			else -> error("$this.scriptLine")
		}

val Native.literal: Literal
	get() =
		when (this) {
			is StringNative -> literal(string)
			is IntNative -> literal(int)
			is DoubleNative -> literal(double)
			else -> error("$this.literal")
		}
