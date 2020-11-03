package leo21.evaluated

import leo14.Script

val Script.evaluate: Script
	get() =
		emptyEvaluator.plus(this).evaluated.script