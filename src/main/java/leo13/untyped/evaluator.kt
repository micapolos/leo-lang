package leo13.untyped

import leo.base.fold
import leo13.LeoStruct
import leo13.script.*

val evaluatorName = "evaluator"

val evaluatorReader: Reader<Evaluator>
	get() =
	reader(evaluatorName, contextReader, evaluatedReader, ::evaluator)

val evaluatorWriter: Writer<Evaluator>
	get() =
	writer(
		contextName,
		field(contextWriter) { context },
		field(evaluatedWriter) { evaluated })

data class Evaluator(val context: Context, val evaluated: Evaluated) :
	LeoStruct(evaluatorName, context, evaluated) {
	override fun toString() = super.toString()
}

fun evaluator(context: Context = context(), evaluated: Evaluated = evaluated(script())) =
	Evaluator(context, evaluated)

fun Evaluator.plus(script: Script): Evaluator =
	fold(script.lineSeq) { plus(it) }

fun Evaluator.plus(line: ScriptLine): Evaluator =
	if (line.name == "meta") plusMeta(line.rhs)
	else evaluated.script.normalizedLineOrNull(line)
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
	switchReader
		.unsafeBodyValue(switchScript)
		.resolveCaseRhsOrNull(evaluated.script)
		?.let { caseRhs -> evaluator(context, evaluated(context.evaluate(caseRhs))) }

fun Evaluator.plusDefineOrNull(script: Script): Evaluator? =
	context
		.resolveDefineOrNull(script)
		?.let { evaluator(it) }

fun Evaluator.plusQuoteOrNull(script: Script): Evaluator =
	fold(script.lineSeq) { plusQuoted(it) }

fun Evaluator.plusMeta(script: Script): Evaluator =
	fold(script.lineSeq) { plusQuoted(it.name lineTo context.evaluate(it.rhs)) }

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
				.bodyOrNull(given)
				?.let { body ->
					body.context
						.plus(binding(key(script("given")), value(script("given" lineTo given))))
						.evaluate(body.script)
						.let { script -> evaluator(context, evaluated(script)) }
				}
		}

fun Evaluator.applyBindingOrNull(line: ScriptLine): Evaluator? =
	context
		.valueOrNull(evaluated.script.plus(line))
		?.let { value -> evaluator(context, Evaluated(value.script)) }

fun Evaluator.plusStatic(line: ScriptLine): Evaluator =
	evaluator(context, evaluated(evaluated.script.resolve(line)))

fun Evaluator.plusQuoted(line: ScriptLine): Evaluator =
	evaluator(context, evaluated(evaluated.script.plus(line)))
