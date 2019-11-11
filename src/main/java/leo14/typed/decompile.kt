package leo14.typed

import leo14.Script
import leo14.ScriptLine
import leo14.literal

val Any.anyScriptLine: ScriptLine
	get() = when (this) {
		is String -> leo14.line(literal(this))
		is Int -> leo14.line(literal(this))
		is Double -> leo14.line(literal(this))
		else -> leo14.line(literal("any($this)"))
	}

val Typed<Any>.anyDecompile: Script
	get() =
		decompile { anyScriptLine }