package leo21.token.evaluator

import leo15.dsl.*
import leo21.evaluated.Evaluated
import leo21.token.processor.evaluatorNode
import leo21.token.processor.process
import leo21.token.processor.processor

fun evaluator(f: F): Evaluator =
	emptyEvaluatorNode.processor.process(f).evaluatorNode.rootEvaluator

fun evaluated(f: F): Evaluated =
	evaluator(f).evaluated
