package leo21.token.evaluator

import leo14.Script
import leo15.dsl.*
import leo21.evaluated.Evaluated
import leo21.evaluated.script
import leo21.token.processor.evaluatorNode
import leo21.token.processor.process
import leo21.token.processor.processor

fun evaluator(f: F): Evaluator =
	emptyEvaluatorNode.processor.process(f).evaluatorNode.rootEvaluator

fun evaluated(f: F): Evaluated =
	evaluator(f).evaluated

fun evaluate(f: F): Script =
	evaluator(f).evaluated.script
