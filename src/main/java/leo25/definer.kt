package leo25

import leo.base.fold
import leo.base.reverse
import leo14.*

data class Definer(
	val context: Context,
	val script: Script
)

fun Context.definer(script: Script = script()) =
	Definer(this, script)

fun Context.define(script: Script): Context =
	definer().fold(script.lineSeq.reverse) { plus(it) }.context

fun Definer.plus(scriptLine: ScriptLine): Definer =
	when (scriptLine) {
		is FieldScriptLine -> plus(scriptLine.field)
		is LiteralScriptLine -> scriptPlus(scriptLine)
	}

fun Definer.scriptPlus(scriptLine: ScriptLine): Definer =
	context.definer(script.plus(scriptLine))

fun Definer.plus(scriptField: ScriptField): Definer =
	null
		?: plusSpecialOrNull(scriptField)
		?: scriptPlus(line(scriptField))

fun Definer.plusSpecialOrNull(scriptField: ScriptField): Definer? =
	when (scriptField.string) {
		doName -> plusGives(scriptField.rhs)
		isName -> plusIs(scriptField.rhs)
		else -> null
	}

fun Definer.plusGives(script: Script): Definer =
	context
		.plus(this.script, binding(context.function(body(script))))
		.definer()

fun Definer.plusIs(script: Script): Definer? =
	context
		.plus(this.script, binding(context.value(script)))
		.definer()
