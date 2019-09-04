package leo13.untyped

import leo.base.fold
import leo13.LeoStruct
import leo13.script.*

data class evaluator(
	val context: context = context(),
	val evaluated: evaluated = evaluated(script())) : LeoStruct("evaluator", context, evaluated) {
	override fun toString() = super.toString()
}

fun evaluator.plus(script: Script): evaluator =
	fold(script.lineSeq) { plus(it) }

fun evaluator.plus(line: ScriptLine): evaluator =
	evaluated.script.normalizedLineOrNull(line)
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
	plusResolved(line.name lineTo context.evaluate(line.rhs).script)

fun evaluator.plusResolved(line: ScriptLine): evaluator =
	when (line.name) {
		"as" -> plusAs(line.rhs)
		"define" -> plusDefineOrNull(line.rhs)
		"switch" -> plusSwitchOrNull(line.rhs)
		else -> null
	} ?: plusResolvedOther(line)

fun evaluator.plusAs(script: Script): evaluator =
	evaluator(
		context.plus(binding(key(evaluated.script), value(script.normalize))),
		evaluated(script()))

fun evaluator.plusSwitchOrNull(switchScript: Script): evaluator? =
	evaluated.script
		.resolveCaseOrNull(switchScript)
		?.let { case -> evaluator(context, context.evaluate(evaluated.script.plus(case))) }

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
		.bodyOrNull(evaluated.script.plus(line))
		?.let { body -> plus(body.script) }

fun evaluator.applyBindingOrNull(line: ScriptLine): evaluator? =
	context
		.bindings
		.valueOrNull(evaluated.script.plus(line))
		?.let { value -> evaluator(context, evaluated(value.script)) }

fun evaluator.plusStatic(line: ScriptLine): evaluator =
	evaluator(context, evaluated(evaluated.script.resolve(line)))