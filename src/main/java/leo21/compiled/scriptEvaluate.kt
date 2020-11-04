package leo21.compiled

import leo14.Script
import leo21.evaluated.script
import leo21.typed.evaluated

val Script.evaluate: Script
	get() =
		typed.evaluated.script