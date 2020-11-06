package leo21.token.evaluator

import leo14.Fragment
import leo14.FragmentParent
import leo14.begin
import leo14.fragment
import leo14.parent
import leo21.evaluator.script

val TokenEvaluator.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(evaluator.evaluated.script)

val EvaluatedParent.fragmentParent: FragmentParent
	get() =
		when (this) {
			is EvaluatorNameEvaluatedParent -> evaluator.fragment.parent(begin(name))
			is EvaluatorDoEvaluatedParent -> evaluator.fragment.parent(begin("do"))
		}

