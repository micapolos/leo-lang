package leo21.evaluator

import leo14.Script

val Script.evaluate: Script
	get() =
		emptyEvaluator.plus(this).evaluated.script