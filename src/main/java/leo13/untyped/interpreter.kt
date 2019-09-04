package leo13.untyped

import leo.base.fold
import leo.base.notNullIf
import leo13.LeoStruct
import leo13.script.*

data class interpreter(val evaluator: evaluator = evaluator()) : LeoStruct("interpreter", evaluator) {
	override fun toString() = super.toString()
}

fun interpreter.plus(script: Script): interpreter =
	fold(script.lineSeq) { plus(it) }

fun interpreter.plus(line: ScriptLine): interpreter =
	plusOrNull(line) ?: plusFallback(line)

fun interpreter.plusOrNull(line: ScriptLine): interpreter? =
	when (line.name) {
		"interpreter" -> plusInterpreterOrNull(line.rhs)
		else -> null
	}

fun interpreter.plusInterpreterOrNull(script: Script): interpreter? =
	notNullIf(script.isEmpty) {
		interpreter(evaluator(evaluator.context, evaluated(script(scriptableLine))))
	}

fun interpreter.plusFallback(line: ScriptLine): interpreter =
	interpreter(evaluator.plus(line))
