package leo21.compiler

import leo14.Script
import leo21.evaluator.script
import leo21.evaluator.evaluated

val Script.evaluate: Script
	get() =
		compiled.evaluated.script