package leo21

import leo14.Script
import leo21.evaluated.script
import leo21.token.evaluator.emptyEvaluatorNode
import leo21.token.processor.evaluatorNode
import leo21.token.processor.plus
import leo21.token.processor.processor

val Script.evaluate: Script
	get() =
		emptyEvaluatorNode
			.processor
			.plus(this)
			.evaluatorNode
			.evaluator
			.evaluated
			.script

