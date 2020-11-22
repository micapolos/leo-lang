package leo21.token.evaluator

import leo14.FragmentParent
import leo14.Script
import leo14.begin
import leo14.fragment
import leo14.parent
import leo21.evaluated.script
import leo21.token.strings.valueKeyword

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
			is EvaluatorNodeApplyEvaluatorParent -> evaluatorNode.printFragment.parent(begin("apply".valueKeyword))
		}

val EvaluatorNodeBegin.printFragmentParent: FragmentParent
	get() =
		evaluatorNode.printFragment.parent(begin(name))

val EvaluatorNodeDo.printFragmentParent: FragmentParent
	get() =
		evaluatorNode.printFragment.parent(begin("do".valueKeyword))
