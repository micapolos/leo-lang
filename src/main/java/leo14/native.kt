package leo14

import leo14.native.BooleanNative
import leo14.native.IntNative
import leo14.native.Native
import leo14.native.StringNative

val Native.scriptLine: ScriptLine
	get() =
		when (this) {
			is BooleanNative -> "$boolean" lineTo script()
			is StringNative -> line(literal(string))
			is IntNative -> line(literal(int))
			else -> error("$this.scriptLine")
		}
