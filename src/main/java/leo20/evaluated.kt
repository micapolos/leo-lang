package leo20

import leo.base.fold
import leo.base.ifOrNull
import leo.base.mapFirstOrNull
import leo.base.reverse
import leo13.onlyOrNull
import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.NumberLiteral
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.StringLiteral
import leo14.fieldOrNull
import leo14.isEmpty
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
	when (literal) {
		is StringLiteral -> Evaluated(scope, scope.resolve(value.plus(leo20.line(literal.string))))
		is NumberLiteral -> Evaluated(scope, scope.resolve(value.plus(line(literal.number.bigDecimal))))
	}

fun Evaluated.plus(scriptField: ScriptField): Evaluated =
	if (scriptField.rhs.isEmpty) plus(scriptField.string)
	else when (scriptField.string) {
		"function" -> plusFunction(scriptField.rhs)
		"apply" -> plusApplyOrNull(scriptField.rhs)
		"do" -> plusDo(scriptField.rhs)
		"switch" -> plusSwitchOrNull(scriptField.rhs)
		"define" -> plusDefineOrNull(scriptField.rhs)
		else -> plusResolve(scriptField)
	} ?: plusQuoted(scriptField)

fun Evaluated.plusQuoted(scriptLine: ScriptLine): Evaluated =
	Evaluated(scope, value.plus(scriptLine))

fun Evaluated.plusQuoted(scriptField: ScriptField): Evaluated =
	plus(line(scriptField))

fun Evaluated.plusResolve(scriptField: ScriptField): Evaluated =
	plusResolve(scriptField.string lineTo scope.value(scriptField.rhs))

fun Evaluated.plusResolve(line: Line): Evaluated =
	Evaluated(scope, scope.resolve(value.plus(line)))

fun Evaluated.plusFunction(script: Script): Evaluated =
	Evaluated(scope, value.plus(line(scope.function(script))))

fun Evaluated.plusApplyOrNull(script: Script): Evaluated? =
	value.applyOrNull(scope.value(script))?.let { Evaluated(scope, it) }

fun Evaluated.plusDo(script: Script): Evaluated =
	Evaluated(scope, scope.function(script).apply(value))

fun Evaluated.plusSwitchOrNull(script: Script): Evaluated? =
	value.bodyOrNull?.lineStack?.onlyOrNull?.selectName?.let { selectName ->
		script.lineSeq.mapFirstOrNull {
			fieldOrNull?.let { field ->
				ifOrNull(field.string == selectName) {
					scope.function(field.rhs).apply(value.bodyOrNull?.lineStack?.onlyOrNull?.fieldOrNull?.rhs!!)
				}
			}
		}
	}?.let { Evaluated(scope, it) }

fun Evaluated.plusDefineOrNull(script: Script): Evaluated? =
	scope.defineOrNull(script)?.evaluated(value())

fun Evaluated.plus(name: String): Evaluated =
	Evaluated(
		scope,
		null
			?: value.getOrNull(name)
			?: scope.resolveOrNull(value.plus(name lineTo value()))
			?: value.make(name))
