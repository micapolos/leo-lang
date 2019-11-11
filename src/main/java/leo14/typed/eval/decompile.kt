package leo14.typed.eval

import leo14.ScriptLine
import leo14.line
import leo14.literal
import leo14.typed.Typed
import leo14.typed.decompile

val Any.anyScriptLine: ScriptLine
	get() = when (this) {
		is String -> line(literal(this))
		is Int -> line(literal(this))
		is Double -> line(literal(this))
		else -> line(literal("any($this)"))
	}

val Typed<Any>.decompile get() = decompile { anyScriptLine }