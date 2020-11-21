package leo21.token.evaluator

import leo14.FragmentParent
import leo14.Script
import leo14.begin
import leo14.fragment
import leo14.parent
import leo21.evaluator.script
import leo21.token.strings.applyKeyword
import leo21.token.strings.doKeyword

val Evaluator.printScript: Script
	get() =
		evaluated.script

val EvaluatorNode.printFragment
	get() =
		parentOrNull?.printFragmentParent.fragment(evaluator.printScript)

val EvaluatorParent.printFragmentParent: FragmentParent
	get() =
		when (this) {
			is EvaluatorNodeBeginEvaluatorParent -> evaluatorNodeBegin.printFragmentParent
			is EvaluatorNodeDoEvaluatorParent -> evaluatorNodeDo.printFragmentParent
			is EvaluatorNodeApplyEvaluatorParent -> evaluatorNode.printFragment.parent(begin(applyKeyword))
		}

val EvaluatorNodeBegin.printFragmentParent: FragmentParent
	get() =
		evaluatorNode.printFragment.parent(begin(name))

val EvaluatorNodeDo.printFragmentParent: FragmentParent
	get() =
		evaluatorNode.printFragment.parent(begin(doKeyword))
