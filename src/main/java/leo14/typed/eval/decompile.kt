package leo14.typed.eval

import leo14.ScriptLine
import leo14.line
import leo14.number
import leo14.typed.Typed
import leo14.typed.decompile

val Any.anyDecompile: ScriptLine
	get() = when (this) {
		is String -> line(this)
		is Int -> line(number(this))
		is Double -> line(number(this))
		else -> line("any($this)")
	}

val Typed<Any>.decompile get() = decompile { anyDecompile }