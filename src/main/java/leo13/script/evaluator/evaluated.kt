package leo13.script.evaluator

import leo13.Script
import leo13.ScriptLine
import leo13.asScript
import leo13.plus

data class Evaluated(val script: Script) {
	override fun toString() = asScript.toString()
	val asScript get() = script.asScript
}

data class LineEvaluated(val line: ScriptLine)
data class EvaluatedLine(val name: String, val rhs: Evaluated)

fun evaluated(script: Script) = Evaluated(script)
fun evaluated(line: ScriptLine) = LineEvaluated(line)
infix fun String.lineTo(rhs: Evaluated) = EvaluatedLine(this, rhs)

fun Evaluated.push(evaluated: LineEvaluated) = evaluated(script.plus(evaluated.line))
