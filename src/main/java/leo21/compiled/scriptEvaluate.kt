package leo21.compiled

import leo14.Script
import leo21.typed.evaluate
import leo21.typed.script

val Script.evaluate: Script
	get() =
		typed.evaluate.script