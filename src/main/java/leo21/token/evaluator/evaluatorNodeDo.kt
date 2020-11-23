package leo21.token.evaluator

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo
import leo14.script
import leo21.evaluated.Evaluated

data class EvaluatorNodeDo(val evaluatorNode: EvaluatorNode) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "do" lineTo script(evaluatorNode.reflectScriptLine)
}

fun EvaluatorNodeDo.end(rhs: Evaluated): EvaluatorNode =
	evaluatorNode.do_(rhs)