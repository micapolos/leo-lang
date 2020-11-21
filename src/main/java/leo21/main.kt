package leo21

import leo14.run
import leo21.token.evaluator.emptyEvaluatorNode
import leo21.token.processor.processor
import leo21.token.processor.stringCharReducer

fun main() {
	run(emptyEvaluatorNode.processor.stringCharReducer)
}
