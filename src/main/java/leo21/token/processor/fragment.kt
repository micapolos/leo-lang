package leo21.token.processor

import leo14.Fragment
import leo21.token.evaluator.fragment

val TokenProcessor.fragment: Fragment
	get() =
		when (this) {
			is CompilerTokenProcessor -> TODO()
			is EvaluatorTokenProcessor -> evaluator.fragment
		}
