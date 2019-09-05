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
		"macro" -> plusResolved(line)
		"switch" -> plusResolved(line)
		"quote" -> plusResolved(line)
		else -> plusOther(line)
	}

fun evaluator.plusOther(line: ScriptLine): evaluator =
	plusResolved(line.name lineTo context.evaluate(line.rhs))

fun evaluator.plusResolved(line: ScriptLine): evaluator =
	when (line.name) {
		"as" -> plusAs(line.rhs)
		"define" -> plusDefineOrNull(line.rhs)
		"evaluate" -> plusEvaluateOrNull(line.rhs)
		"macro" -> plusMacro(line.rhs)
		"switch" -> plusSwitchOrNull(line.rhs)
		"quote" -> plusQuoteOrNull(line.rhs)
		else -> null
	} ?: plusResolvedOther(line)

fun evaluator.plusAs(script: Script): evaluator =
	evaluator(
		context.plus(binding(key(script.normalize), value(evaluated.script))),
		evaluated(script()))

fun evaluator.plusEvaluateOrNull(script: Script): evaluator =
	plus(script)

fun evaluator.plusSwitchOrNull(switchScript: Script): evaluator? =
	switchScript
		.parseSwitch
		?.resolveCaseRhsOrNull(evaluated.script)
		?.let { caseRhs -> evaluator(context, evaluated(context.evaluate(caseRhs))) }

fun evaluator.plusDefineOrNull(script: Script): evaluator? =
	context
		.resolveDefineOrNull(script)
		?.let { evaluator(it) }

fun evaluator.plusMacro(script: Script): evaluator =
	evaluator(context.parent)
		.plus(script)
		.let { macroEvaluator -> evaluator(context.withParent(macroEvaluator.context), evaluated) }

fun evaluator.plusQuoteOrNull(script: Script): evaluator =
	fold(script.lineSeq) { plusQuoted(it) }

fun evaluator.plusResolvedOther(line: ScriptLine): evaluator =
	null
		?: applyBindingOrNull(line)
		?: applyFunctionOrNull(line)
		?: plusStatic(line)

fun evaluator.applyFunctionOrNull(line: ScriptLine): evaluator? =
	evaluated
		.script
		.plus(line)
		.let { given ->
			context
				.functions
				.bodyOrNull(given)
				?.let { body ->
					evaluator(
						context,
						evaluated(
							context
								.plus(binding(key(script("given")), value(script("given" lineTo given))))
								.evaluate(body.script)))
				}
		}

fun evaluator.applyBindingOrNull(line: ScriptLine): evaluator? =
	context
		.bindings
		.valueOrNull(evaluated.script.plus(line))
		?.let { value -> evaluator(context, evaluated(value.script)) }

fun evaluator.plusStatic(line: ScriptLine): evaluator =
	evaluator(context, evaluated(evaluated.script.resolve(line)))

fun evaluator.plusQuoted(line: ScriptLine): evaluator =
	evaluator(context, evaluated(evaluated.script.plus(line)))
