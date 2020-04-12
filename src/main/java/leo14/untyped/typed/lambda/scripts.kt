package leo14.untyped.typed.lambda

import leo14.Script

val Script.eval: Script
	get() =
		emptyEvaluator.plus(this).script