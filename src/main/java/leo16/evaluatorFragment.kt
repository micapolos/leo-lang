package leo16

import leo14.Fragment
import leo14.FragmentParent
import leo14.fragment
import leo14.parent

val Evaluator.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(script.leo14Script)

val EvaluatorParent.fragmentParent: FragmentParent
	get() =
		evaluator.fragment.parent(leo14.begin(word))
