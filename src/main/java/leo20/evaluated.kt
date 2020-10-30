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
import leo14.line
import leo14.lineSeq
import leo14.lineTo
import leo14.rhsOrNull

data class Evaluated(
	val scope: Scope,
	val value: Value
)

fun Scope.evaluated(value: Value) = Evaluated(this, value)

fun Scope.value(script: Script): Value =
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
		"give" -> plusGive(scriptField.rhs)
		"do" -> plusDoOrNull(scriptField.rhs)
		"function" -> plusFunction(scriptField.rhs)
		"get" -> plusGetOrNull(scriptField.rhs)
		"make" -> plusMakeOrNull(scriptField.rhs)
		"quote" -> plusQuote(scriptField.rhs)
		"save" -> plusSave(scriptField.rhs)
		"switch" -> plusSwitchOrNull(scriptField.rhs)
		"test" -> plusTestOrNull(scriptField.rhs)
		else -> plusContent(line(scriptField))
	} ?: plusFallback(scriptField.valueLine)

fun Evaluated.plusResolve(line: Line): Evaluated =
	copy(value = scope.dictionary.resolve(value.plus(line)))

fun Evaluated.plusFallback(line: Line): Evaluated =
	plusError(line)

fun Evaluated.plusQuoted(line: Line): Evaluated =
	copy(value = value.plus(line))

fun Evaluated.plusError(line: Line): Evaluated =
	error(("error" lineTo value.plus(line).script).toString())

fun Evaluated.plusFunction(script: Script): Evaluated =
	copy(value = value.plus(line(scope.function(body(script)))))

fun Evaluated.plusApplyOrNull(script: Script): Evaluated? =
	value.applyOrNull(scope.value(script))?.let { copy(value = it) }

fun Evaluated.plusMakeOrNull(script: Script): Evaluated? =
	value.makeOrNull(script)?.let { copy(value = it) }

fun Evaluated.plusGetOrNull(script: Script): Evaluated? =
	getValueOrNull(script)?.let { copy(value = it) }

fun Evaluated.plusGive(script: Script): Evaluated =
	copy(value = scope.push(value).value(script))

fun Evaluated.plusDoOrNull(script: Script): Evaluated? =
	scope.dictionary
		.resolveOrNull(value.plus(scope.push(value).evaluated(value()).plusContent(script).value))
		?.let { copy(value = it) }

fun Evaluated.plusSwitchOrNull(script: Script): Evaluated? =
	value.contentOrNull?.lineStack?.onlyOrNull?.let { switchLine ->
		script.lineSeq.mapFirstOrNull {
			fieldOrNull?.let { caseField ->
				caseField.string.let { caseName ->
					caseField.rhs.rhsOrNull("does")?.let { caseBody ->
						ifOrNull(caseName == switchLine.selectName) {
							scope.push(value(switchLine)).value(caseBody)
						}
					}
				}
			}
		}
	}?.let { copy(value = it) }

fun Evaluated.plusDefineOrNull(script: Script): Evaluated? =
	scope.defineOrNull(script)?.evaluated(value())

fun Evaluated.plusTestOrNull(script: Script): Evaluated? =
	notNullIf(value == value()) { also { scope.test(script) } }

fun Evaluated.getValueOrNull(script: Script): Value? =
	if (value == value()) scope.getOrNull(script)
	else value.getOrNull(script)

fun Evaluated.plusContent(script: Script): Evaluated =
	fold(script.lineSeq.reverse) { plusContent(it) }

fun Evaluated.plusContent(scriptLine: ScriptLine): Evaluated =
	when (scriptLine) {
		is LiteralScriptLine -> copy(value = value.plus(scriptLine.literal.line))
		is FieldScriptLine -> copy(value = value.plus(scriptLine.field.string lineTo scope.push(value).value(scriptLine.field.rhs)))
	}

fun Evaluated.plusQuote(script: Script): Evaluated =
	fold(script.lineSeq.reverse) { plusQuote(it) }

fun Evaluated.plusQuote(scriptLine: ScriptLine): Evaluated =
	plusQuoted(scriptLine.valueLine)

fun Evaluated.plusSave(script: Script): Evaluated =
	copy(scope = scope.push(scope.value(script)))

