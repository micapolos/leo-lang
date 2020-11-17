package leo21.token.evaluator

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo
import leo14.script

data class EvaluatorNodeDo(val evaluatorNode: EvaluatorNode) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "do" lineTo script(evaluatorNode.reflectScriptLine)
}

fun EvaluatorNodeDo.end(evaluator: Evaluator): EvaluatorNode =
	evaluatorNode.do_(evaluator)