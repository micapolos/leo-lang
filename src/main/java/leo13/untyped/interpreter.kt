package leo13.untyped

import leo.base.fold
import leo.base.notNullIf
import leo13.LeoStruct
import leo13.script.*

data class Interpreter(val evaluator: Evaluator) : LeoStruct("interpreter", evaluator) {
	override fun toString() = super.toString()
}

fun interpreter(evaluator: Evaluator = evaluator()) = Interpreter(evaluator)

fun Interpreter.plus(script: Script): Interpreter =
	fold(script.lineSeq) { plus(it) }

fun Interpreter.plus(line: ScriptLine): Interpreter =
	plusOrNull(line) ?: plusFallback(line)

fun Interpreter.plusOrNull(line: ScriptLine): Interpreter? =
	when (line.name) {
		"interpreter" -> plusInterpreterOrNull(line.rhs)
		else -> null
	}

fun Interpreter.plusInterpreterOrNull(script: Script): Interpreter? =
	notNullIf(script.isEmpty) {
		Interpreter(Evaluator(evaluator.context, Evaluated(script(scriptableLine))))
	}

fun Interpreter.plusFallback(line: ScriptLine): Interpreter =
	Interpreter(evaluator.plus(line))
