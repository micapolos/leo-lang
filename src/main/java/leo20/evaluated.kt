package leo20

import leo.base.fold
import leo.base.ifOrNull
import leo.base.mapFirstOrNull
import leo.base.notNullIf
import leo.base.reverse
import leo13.onlyOrNull
import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.fieldOrNull
import leo14.isEmpty
import leo14.line
import leo14.lineSeq
import leo14.lineTo
import leo14.rhsOrNull

data class Evaluated(
	val dictionary: Dictionary,
	val value: Value
)

fun Dictionary.evaluated(value: Value) = Evaluated(this, value)

fun Dictionary.value(script: Script): Value =
	Evaluated(this, value()).plus(script).value

fun Evaluated.plus(script: Script): Evaluated =
	fold(script.lineSeq.reverse) { plus(it) }

fun Evaluated.plus(scriptLine: ScriptLine): Evaluated =
	when (scriptLine) {
		is LiteralScriptLine -> plus(scriptLine.literal)
		is FieldScriptLine -> plus(scriptLine.field)
	}

fun Evaluated.plus(literal: Literal): Evaluated =
	plusQuoted(leo20.line(literal))

fun Evaluated.plus(scriptField: ScriptField): Evaluated =
	when (scriptField.string) {
		"apply" -> plusApplyOrNull(scriptField.rhs)
		"content" -> plusContent(scriptField.rhs)
		"define" -> plusDefineOrNull(scriptField.rhs)
		"do" -> plusDo(scriptField.rhs)
		"resolve" -> plusResolveOrNull(scriptField.rhs)
		"fail" -> plusFailOrNull(scriptField.rhs)
		"function" -> plusFunction(scriptField.rhs)
		"get" -> plusGetOrNull(scriptField.rhs)
		"make" -> plusMakeOrNull(scriptField.rhs)
		"quote" -> plusQuote(scriptField.rhs)
		"save" -> plusSave(scriptField.rhs)
		"switch" -> plusSwitchOrNull(scriptField.rhs)
		"test" -> plusTestOrNull(scriptField.rhs)
		else -> plusResolve(scriptField)
	} ?: plusFallback(scriptField.valueLine)

fun Evaluated.plusResolve(scriptField: ScriptField): Evaluated =
	plusResolve(scriptField.string lineTo dictionary.push(value).value(scriptField.rhs))


fun Evaluated.plusResolve(line: Line): Evaluated =
	copy(value = dictionary.resolve(value.plus(line)))

fun Evaluated.plusFallback(line: Line): Evaluated =
	plusError(line)

fun Evaluated.plusQuoted(line: Line): Evaluated =
	copy(value = value.plus(line))

fun Evaluated.plusFailOrNull(script: Script): Evaluated? =
	notNullIf(script.isEmpty) {
		error(("error" lineTo value.script).toString())
	}

fun Evaluated.plusError(line: Line): Evaluated =
	error(("error" lineTo value.plus(line).script).toString())

fun Evaluated.plusFunction(script: Script): Evaluated =
	copy(value = value.plus(line(dictionary.function(body(script)))))

fun Evaluated.plusApplyOrNull(script: Script): Evaluated? =
	value.applyOrNull(dictionary.value(script))?.let { copy(value = it) }

fun Evaluated.plusMakeOrNull(script: Script): Evaluated? =
	value.makeOrNull(script)?.let { copy(value = it) }

fun Evaluated.plusGetOrNull(script: Script): Evaluated? =
	getValueOrNull(script)?.let { copy(value = it) }

fun Evaluated.plusDo(script: Script): Evaluated =
	copy(value = dictionary.push(value).value(script))

fun Evaluated.plusResolveOrNull(script: Script): Evaluated? =
	dictionary
		.resolveOrNull(value.plus(dictionary.push(value).evaluated(value()).plusContent(script).value))
		?.let { copy(value = it) }

fun Evaluated.plusSwitchOrNull(script: Script): Evaluated? =
	value.contentOrNull?.lineStack?.onlyOrNull?.let { switchLine ->
		script.lineSeq.mapFirstOrNull {
			fieldOrNull?.let { caseField ->
				caseField.string.let { caseName ->
					caseField.rhs.rhsOrNull("does")?.let { caseBody ->
						ifOrNull(caseName == switchLine.selectName) {
							dictionary.push(value(switchLine)).value(caseBody)
						}
					}
				}
			}
		}
	}?.let { copy(value = it) }

fun Evaluated.plusDefineOrNull(script: Script): Evaluated? =
	dictionary.defineOrNull(script)?.evaluated(value)

fun Evaluated.plusTestOrNull(script: Script): Evaluated? =
	notNullIf(value == value()) { also { dictionary.test(script) } }

fun Evaluated.getValueOrNull(script: Script): Value? =
	if (value == value()) dictionary.getOrNull(script)
	else value.getOrNull(script)

fun Evaluated.plusContent(script: Script): Evaluated =
	fold(script.lineSeq.reverse) { plusContent(it) }

fun Evaluated.plusContent(scriptLine: ScriptLine): Evaluated =
	when (scriptLine) {
		is LiteralScriptLine -> copy(value = value.plus(scriptLine.literal.line))
		is FieldScriptLine -> copy(value = value.plus(scriptLine.field.string lineTo dictionary.push(value).value(scriptLine.field.rhs)))
	}

fun Evaluated.plusQuote(script: Script): Evaluated =
	fold(script.lineSeq.reverse) { plusQuote(it) }

fun Evaluated.plusQuote(scriptLine: ScriptLine): Evaluated =
	plusQuoted(scriptLine.valueLine)

fun Evaluated.plusSave(script: Script): Evaluated =
	copy(dictionary = dictionary.push(dictionary.value(script)))

