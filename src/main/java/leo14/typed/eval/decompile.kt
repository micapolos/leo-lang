package leo14.typed.eval

import leo14.Script
import leo14.script
import leo14.typed.Typed
import leo14.typed.decompile

val Any.anyDecompile: Script
	get() = when (this) {
		is String -> script(this)
		is Int -> script(this)
		is Double -> script(this)
		else -> script("any($this)")
	}

val Typed<Any>.decompile get() = decompile { anyDecompile }