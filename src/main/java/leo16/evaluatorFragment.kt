package leo16

import leo14.*

val Evaluator.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(evaluated.reflectValue.printed.script)

val EvaluatorParent.fragmentParent: FragmentParent
	get() =
		evaluator.fragment.parent(begin(word))
