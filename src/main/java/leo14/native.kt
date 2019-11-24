package leo14

import leo14.native.BooleanNative
import leo14.native.Native
import leo14.native.NumberNative
import leo14.native.StringNative

val Native.scriptLine: ScriptLine
	get() =
		when (this) {
			is BooleanNative -> "$boolean" lineTo script()
			is StringNative -> line(literal(string))
			is NumberNative -> line(literal(number))
			else -> error("$this.scriptLine")
		}

val Native.literal: Literal
	get() =
		when (this) {
			is StringNative -> literal(string)
			is NumberNative -> literal(number)
			else -> error("$this.literal")
		}
