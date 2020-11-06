package leo21.token.processor

import leo14.Script
import leo21.evaluator.Evaluated

val Script.evaluated: Evaluated
	get() =
		emptyEvaluatorTokenProcessor.plus(this).evaluated
