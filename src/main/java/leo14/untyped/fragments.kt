package leo14.untyped

import leo14.Fragment
import leo14.FragmentParent
import leo14.fragment
import leo14.parent

val Tokenizer.fragment: Fragment
	get() =
		parentOrNull?.fragmentParent.fragment(evaluator.program.script)

val TokenizerParent.fragmentParent: FragmentParent
	get() =
		tokenizer.fragment.parent(begin)
