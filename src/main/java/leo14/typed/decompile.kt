package leo14.typed

import leo14.Script
import leo14.typed.eval.anyScriptLine

val Typed<Any>.anyDecompile: Script
	get() =
		decompile { anyScriptLine }