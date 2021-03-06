package leo21.token.evaluator

import leo14.ScriptLine
import leo14.Scriptable
import leo14.anyReflectScriptLine
import leo14.lineTo
import leo14.script
import leo21.evaluated.Evaluated
import leo21.token.processor.Processor
import leo21.token.processor.processor

sealed class EvaluatorParent : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "parent" lineTo script(
			when (this) {
				is EvaluatorNodeBeginEvaluatorParent -> evaluatorNodeBegin.anyReflectScriptLine
				is EvaluatorNodeDoEvaluatorParent -> evaluatorNodeDo.anyReflectScriptLine
				is EvaluatorNodeApplyEvaluatorParent -> "apply" lineTo script(evaluatorNode.reflectScriptLine)
			}
		)
}

data class EvaluatorNodeBeginEvaluatorParent(val evaluatorNodeBegin: EvaluatorNodeBegin) : EvaluatorParent() {
	override fun toString() = super.toString()
}

data class EvaluatorNodeDoEvaluatorParent(val evaluatorNodeDo: EvaluatorNodeDo) : EvaluatorParent() {
	override fun toString() = super.toString()
}

data class EvaluatorNodeApplyEvaluatorParent(val evaluatorNode: EvaluatorNode) : EvaluatorParent() {
	override fun toString() = super.toString()
}

fun EvaluatorParent.end(rhs: Evaluated): Processor =
	when (this) {
		is EvaluatorNodeBeginEvaluatorParent -> evaluatorNodeBegin.end(rhs).processor
		is EvaluatorNodeDoEvaluatorParent -> evaluatorNodeDo.end(rhs).processor
		is EvaluatorNodeApplyEvaluatorParent -> evaluatorNode.apply(rhs).processor
	}

