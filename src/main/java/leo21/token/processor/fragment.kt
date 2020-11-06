package leo21.token.processor

import leo14.Fragment
import leo21.token.compiler.fragment
import leo21.token.evaluator.fragment

val TokenProcessor.fragment: Fragment
	get() =
		when (this) {
			is CompilerTokenProcessor -> compiler.fragment
			is EvaluatorTokenProcessor -> evaluator.fragment
		}
