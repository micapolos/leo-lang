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
	plusResolve(leo20.line(literal))

fun Evaluated.plus(scriptField: ScriptField): Evaluated =
	when (scriptField.string) {
		"apply" -> plusApplyOrNull(scriptField.rhs)
		"define" -> plusDefineOrNull(scriptField.rhs)
		"do" -> plusDo(scriptField.rhs)
		"function" -> plusFunction(scriptField.rhs)
		"get" -> plusGetOrNull(scriptField.rhs)
		"make" -> plusMakeOrNull(scriptField.rhs)
		"switch" -> plusSwitchOrNull(scriptField.rhs)
		"test" -> plusTestOrNull(scriptField.rhs)
		"with" -> plusWith(scriptField.rhs)
		"quote" -> plusQuote(scriptField.rhs)
		else -> plusResolve(scriptField)
	} ?: plusQuoted(scriptField.valueLine)

fun Evaluated.plusResolve(scriptField: ScriptField): Evaluated =
	plusResolve(scriptField.string lineTo scope.push(value).value(scriptField.rhs))

fun Evaluated.plusResolve(line: Line): Evaluated =
	copy(value = scope.bindings.resolve(value.plus(line)))

fun Evaluated.plusQuoted(line: Line): Evaluated =
	copy(value = value.plus(line))

fun Evaluated.plusFunction(script: Script): Evaluated =
	copy(value = value.plus(line(scope.function(body(script)))))

fun Evaluated.plusApplyOrNull(script: Script): Evaluated? =
	value.applyOrNull(scope.value(script))?.let { copy(value = it) }

fun Evaluated.plusMakeOrNull(script: Script): Evaluated? =
	value.makeOrNull(script)?.let { copy(value = it) }

fun Evaluated.plusGetOrNull(script: Script): Evaluated? =
	getValueOrNull(script)?.let { copy(value = it) }

fun Evaluated.plusDo(script: Script): Evaluated =
	copy(value = scope.push(value).value(script))

fun Evaluated.plusSwitchOrNull(script: Script): Evaluated? =
	value.bodyOrNull?.lineStack?.onlyOrNull?.let { switchLine ->
		script.lineSeq.mapFirstOrNull {
			fieldOrNull?.let { caseField ->
				ifOrNull(caseField.string == switchLine.selectName) {
					scope.evaluated(value(switchLine)).plus(caseField.rhs).value
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

fun Evaluated.plusWith(script: Script): Evaluated =
	fold(script.lineSeq.reverse) { plusWith(it) }

fun Evaluated.plusWith(scriptLine: ScriptLine): Evaluated =
	when (scriptLine) {
		is LiteralScriptLine -> copy(value = value.plus(scriptLine.literal.line))
		is FieldScriptLine -> copy(value = value.plus(scriptLine.field.string lineTo scope.value(scriptLine.field.rhs)))
	}

fun Evaluated.plusQuote(script: Script): Evaluated =
	fold(script.lineSeq.reverse) { plusQuote(it) }

fun Evaluated.plusQuote(scriptLine: ScriptLine): Evaluated =
	plusQuoted(scriptLine.valueLine)
