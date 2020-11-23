package leo21.token.evaluator

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo
import leo14.script
import leo21.evaluated.Evaluated
import leo21.evaluated.lineTo

data class EvaluatorNodeBegin(val evaluatorNode: EvaluatorNode, val name: String) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "begin" lineTo script(evaluatorNode.reflectScriptLine, "name" lineTo script(name))
}

fun EvaluatorNodeBegin.end(rhs: Evaluated): EvaluatorNode =
	evaluatorNode.plus(name lineTo rhs)