package leo13.untyped

import leo13.LeoStruct
import leo13.script.*

data class evaluator(
	val context: context = context(),
	val script: Script = script()) : LeoStruct("evaluator", context, script) {
	override fun toString() = super.toString()
}

fun evaluator.plus(line: ScriptLine): evaluator =
	script.normalizedLineOrNull(line)
		?.let { evaluator(context).plusResolved(it) }
		?: plusNormalized(line)

fun evaluator.plusNormalized(line: ScriptLine): evaluator =
	when (line.name) {
		"switch" -> plusResolved(line)
		"define" -> plusResolved(line)
		else -> plusOther(line)
	}

fun evaluator.plusOther(line: ScriptLine): evaluator =
	plusResolved(line.name lineTo context.evaluate(line.rhs))

fun evaluator.plusResolved(line: ScriptLine): evaluator =
	when (line.name) {
		"switch" -> plusSwitchOrNull(line.rhs)
		"define" -> plusDefineOrNull(line.rhs)
		else -> null
	} ?: plusResolvedOther(line)

fun evaluator.plusSwitchOrNull(switchScript: Script): evaluator? =
	script
		.caseOrNull(switchScript)
		?.let { caseRhs -> evaluator(context, context.evaluate(script.plus(caseRhs))) }

fun evaluator.plusDefineOrNull(script: Script): evaluator? =
	TODO()

fun evaluator.plusResolvedOther(line: ScriptLine): evaluator =
	evaluator(context, script.resolve(line))
