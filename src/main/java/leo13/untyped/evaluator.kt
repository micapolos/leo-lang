package leo13.untyped

import leo.base.fold
import leo13.LeoStruct
import leo13.script.*

data class evaluator(
	val context: context = context(),
	val evaluatedScript: Script = script()) : LeoStruct("evaluator", context, evaluatedScript) {
	override fun toString() = super.toString()
}

fun evaluator.plus(script: Script): evaluator =
	fold(script.lineSeq) { plus(it) }

fun evaluator.plus(line: ScriptLine): evaluator =
	evaluatedScript.normalizedLineOrNull(line)
		?.let { evaluator(context).plusResolved(it) }
		?: plusNormalized(line)

fun evaluator.plusNormalized(line: ScriptLine): evaluator =
	when (line.name) {
		"as" -> plusResolved(line)
		"define" -> plusResolved(line)
		"switch" -> plusResolved(line)
		else -> plusOther(line)
	}

fun evaluator.plusOther(line: ScriptLine): evaluator =
	plusResolved(line.name lineTo context.evaluate(line.rhs))

fun evaluator.plusResolved(line: ScriptLine): evaluator =
	when (line.name) {
		"as" -> plusAs(line.rhs)
		"define" -> plusDefineOrNull(line.rhs)
		"switch" -> plusSwitchOrNull(line.rhs)
		else -> null
	} ?: plusResolvedOther(line)

fun evaluator.plusAs(script: Script): evaluator =
	evaluator(
		context.plus(binding(key(evaluatedScript), value(script))),
		script())

fun evaluator.plusSwitchOrNull(switchScript: Script): evaluator? =
	evaluatedScript
		.resolveCaseOrNull(switchScript)
		?.let { case -> evaluator(context, context.evaluate(evaluatedScript.plus(case))) }

fun evaluator.plusDefineOrNull(script: Script): evaluator? =
	context
		.resolveDefineOrNull(script)
		?.let { evaluator(it) }

fun evaluator.plusResolvedOther(line: ScriptLine): evaluator =
	null
		?: applyBindingOrNull(line)
		?: applyFunctionOrNull(line)
		?: plusStatic(line)

fun evaluator.applyFunctionOrNull(line: ScriptLine): evaluator? =
	context
		.functions
		.bodyOrNull(evaluatedScript.plus(line))
		?.let { body -> plus(body.script) }

fun evaluator.applyBindingOrNull(line: ScriptLine): evaluator? =
	context
		.bindings
		.valueOrNull(evaluatedScript.plus(line))
		?.let { value -> evaluator(context, value.script) }

fun evaluator.plusStatic(line: ScriptLine): evaluator =
	evaluator(context, evaluatedScript.resolve(line))