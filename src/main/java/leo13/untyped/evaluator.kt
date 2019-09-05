package leo13.untyped

import leo.base.fold
import leo13.LeoStruct
import leo13.script.*

data class Evaluator(
	val context: Context,
	val evaluated: Evaluated) : LeoStruct("evaluator", context, evaluated) {
	override fun toString() = super.toString()
}

fun evaluator(context: Context = context(), evaluated: Evaluated = evaluated(script())) =
	Evaluator(context, evaluated)

fun Evaluator.plus(script: Script): Evaluator =
	fold(script.lineSeq) { plus(it) }

fun Evaluator.plus(line: ScriptLine): Evaluator =
	evaluated.script.normalizedLineOrNull(line)
		?.let { evaluator(context).plusResolved(it) }
		?: plusNormalized(line)

fun Evaluator.plusNormalized(line: ScriptLine): Evaluator =
	when (line.name) {
		"as" -> plusResolved(line)
		"define" -> plusResolved(line)
		"macro" -> plusResolved(line)
		"switch" -> plusResolved(line)
		"quote" -> plusResolved(line)
		else -> plusOther(line)
	}

fun Evaluator.plusOther(line: ScriptLine): Evaluator =
	plusResolved(line.name lineTo context.evaluate(line.rhs))

fun Evaluator.plusResolved(line: ScriptLine): Evaluator =
	when (line.name) {
		"as" -> plusAs(line.rhs)
		"define" -> plusDefineOrNull(line.rhs)
		"evaluate" -> plusEvaluateOrNull(line.rhs)
		"switch" -> plusSwitchOrNull(line.rhs)
		"quote" -> plusQuoteOrNull(line.rhs)
		else -> null
	} ?: plusResolvedOther(line)

fun Evaluator.plusAs(script: Script): Evaluator =
	Evaluator(
		context.plus(Binding(Key(script.normalize), Value(evaluated.script))),
		Evaluated(script()))

fun Evaluator.plusEvaluateOrNull(script: Script): Evaluator =
	plus(script)

fun Evaluator.plusSwitchOrNull(switchScript: Script): Evaluator? =
	switchScript
		.parseSwitch
		?.resolveCaseRhsOrNull(evaluated.script)
		?.let { caseRhs -> Evaluator(context, Evaluated(context.evaluate(caseRhs))) }

fun Evaluator.plusDefineOrNull(script: Script): Evaluator? =
	context
		.resolveDefineOrNull(script)
		?.let { evaluator(it) }

fun Evaluator.plusQuoteOrNull(script: Script): Evaluator =
	fold(script.lineSeq) { plusQuoted(it) }

fun Evaluator.plusResolvedOther(line: ScriptLine): Evaluator =
	null
		?: applyBindingOrNull(line)
		?: applyFunctionOrNull(line)
		?: plusStatic(line)

fun Evaluator.applyFunctionOrNull(line: ScriptLine): Evaluator? =
	evaluated
		.script
		.plus(line)
		.let { given ->
			context
				.functions
				.bodyOrNull(given)
				?.let { body ->
					Evaluator(
						context,
						Evaluated(
							context
								.plus(Binding(Key(script("given")), Value(script("given" lineTo given))))
								.evaluate(body.script)))
				}
		}

fun Evaluator.applyBindingOrNull(line: ScriptLine): Evaluator? =
	context
		.bindings
		.valueOrNull(evaluated.script.plus(line))
		?.let { value -> Evaluator(context, Evaluated(value.script)) }

fun Evaluator.plusStatic(line: ScriptLine): Evaluator =
	Evaluator(context, Evaluated(evaluated.script.resolve(line)))

fun Evaluator.plusQuoted(line: ScriptLine): Evaluator =
	Evaluator(context, Evaluated(evaluated.script.plus(line)))
