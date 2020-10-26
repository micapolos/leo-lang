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
import leo14.lineSeq
import leo14.lineTo

data class Evaluated(
	val scope: Scope,
	val value: Value
)

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
		is StringLiteral -> Evaluated(scope, value.plus(line(literal.string)))
		is NumberLiteral -> Evaluated(scope, value.plus(line(literal.number.bigDecimal)))
	}

fun Evaluated.plus(scriptField: ScriptField): Evaluated =
	if (scriptField.rhs.isEmpty) plus(scriptField.string)
	else when (scriptField.string) {
		"function" -> function(scriptField.rhs)
		"apply" -> apply(scriptField.rhs)
		"do" -> do_(scriptField.rhs)
		"switch" -> switch(scriptField.rhs)
		"define" -> define(scriptField.rhs)
		else -> plusAppend(scriptField)
	}

fun Evaluated.plusAppend(scriptField: ScriptField): Evaluated =
	Evaluated(scope, value.plus(scriptField.string lineTo scope.value(scriptField.rhs)))

fun Evaluated.function(script: Script): Evaluated =
	Evaluated(scope, value.plus(line(scope.function(script))))

fun Evaluated.apply(script: Script): Evaluated =
	Evaluated(scope, value.apply(scope.value(script)))

fun Evaluated.do_(script: Script): Evaluated =
	Evaluated(scope, scope.function(script).apply(value))

fun Evaluated.switch(script: Script): Evaluated =
	value.bodyOrNull?.lineStack?.onlyOrNull?.selectName?.let { selectName ->
		script.lineSeq.mapFirstOrNull {
			fieldOrNull?.let { field ->
				ifOrNull(field.string == selectName) {
					scope.function(field.rhs).apply(value.bodyOrNull?.lineStack?.onlyOrNull?.fieldOrNull?.rhs!!)
				}
			}
		}
	}?.let { Evaluated(scope, it) } ?: Evaluated(scope, value.plus("switch" lineTo script))

fun Evaluated.define(script: Script): Evaluated = TODO()

fun Evaluated.plus(name: String): Evaluated =
	Evaluated(
		scope,
		null
			?: value.getOrNull(name)
			?: scope.resolveOrNull(value.plus(name lineTo value()))
			?: value.make(name))
