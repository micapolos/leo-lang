package leo21.token.processor

import leo14.Script
import leo21.evaluator.script

val Script.evaluate: Script
	get() =
		evaluated.script
