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
import leo14.NumberLiteral
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.StringLiteral
import leo14.bigDecimal
import leo14.fieldOrNull
import leo14.isEmpty
import leo14.line
import leo14.lineSeq
import leo14.onlyStringOrNull

data class Evaluated(
	val bindings: Bindings,
	val value: Value
)

fun Bindings.evaluated(value: Value) = Evaluated(this, value)

fun Bindings.value(script: Script): Value =
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
		is StringLiteral -> Evaluated(bindings, bindings.resolve(value.plus(leo20.line(literal.string))))
		is NumberLiteral -> Evaluated(bindings, bindings.resolve(value.plus(line(literal.number.bigDecimal.toDouble().bigDecimal))))
	}

fun Evaluated.plus(scriptField: ScriptField): Evaluated =
	if (scriptField.rhs.isEmpty) plus(scriptField.string)
	else when (scriptField.string) {
		"function" -> plusFunction(scriptField.rhs)
		"apply" -> plusApplyOrNull(scriptField.rhs)
		"do" -> plusDo(scriptField.rhs)
		"make" -> plusMakeOrNull(scriptField.rhs)
		"switch" -> plusSwitchOrNull(scriptField.rhs)
		"define" -> plusDefineOrNull(scriptField.rhs)
		"test" -> plusTestOrNull(scriptField.rhs)
		else -> plusResolve(scriptField)
	} ?: plusQuoted(scriptField)

fun Evaluated.plusQuoted(scriptLine: ScriptLine): Evaluated =
	Evaluated(bindings, value.plus(scriptLine))

fun Evaluated.plusQuoted(scriptField: ScriptField): Evaluated =
	plus(line(scriptField))

fun Evaluated.plusResolve(scriptField: ScriptField): Evaluated =
	plusResolve(scriptField.string lineTo bindings.value(scriptField.rhs))

fun Evaluated.plusResolve(line: Line): Evaluated =
	Evaluated(bindings, bindings.resolve(value.plus(line)))

fun Evaluated.plusFunction(script: Script): Evaluated =
	Evaluated(bindings, value.plus(line(bindings.function(body(script)))))

fun Evaluated.plusApplyOrNull(script: Script): Evaluated? =
	value.applyOrNull(bindings.value(script))?.let { Evaluated(bindings, it) }

fun Evaluated.plusMakeOrNull(script: Script): Evaluated? =
	script.onlyStringOrNull?.let { name ->
		Evaluated(bindings, value.make(name))
	}

fun Evaluated.plusDo(script: Script): Evaluated =
	Evaluated(bindings, bindings.function(body(script)).apply(value))

fun Evaluated.plusSwitchOrNull(script: Script): Evaluated? =
	value.bodyOrNull?.lineStack?.onlyOrNull?.selectName?.let { selectName ->
		script.lineSeq.mapFirstOrNull {
			fieldOrNull?.let { field ->
				ifOrNull(field.string == selectName) {
					bindings.function(body(field.rhs)).apply(value.bodyOrNull?.lineStack?.onlyOrNull?.fieldOrNull?.rhs!!)
				}
			}
		}
	}?.let { Evaluated(bindings, it) }

fun Evaluated.plusDefineOrNull(script: Script): Evaluated? =
	bindings.defineOrNull(script)?.evaluated(value())

fun Evaluated.plusTestOrNull(script: Script): Evaluated? =
	notNullIf(value == value()) { also { bindings.test(script) } }

fun Evaluated.plus(name: String): Evaluated =
	Evaluated(
		bindings,
		null
			?: value.getOrNull(name)
			?: bindings.resolve(value.plus(name lineTo value())))
