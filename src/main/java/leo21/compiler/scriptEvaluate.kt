package leo21.compiler

import leo14.Script
import leo21.evaluated.script
import leo21.evaluated.evaluated

val Script.evaluate: Script
	get() =
		compiled.evaluated.script