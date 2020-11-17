package leo21.token.evaluator

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo
import leo14.script

data class EvaluatorNodeBegin(val evaluatorNode: EvaluatorNode, val name: String) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "begin" lineTo script(evaluatorNode.reflectScriptLine, "name" lineTo script(name))
}

fun EvaluatorNodeBegin.end(evaluator: Evaluator): EvaluatorNode =
	evaluatorNode.end(name fieldTo evaluator)