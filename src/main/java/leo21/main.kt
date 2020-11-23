package leo21

import leo14.run
import leo21.token.evaluator.emptyEvaluatorNode
import leo21.token.processor.processor
import leo21.token.repl.repl
import leo21.token.repl.stringCharReducer

fun main() {
	run(emptyEvaluatorNode.processor.repl.stringCharReducer)
}
