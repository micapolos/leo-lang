package leo25

import leo.base.fold
import leo.base.reverse
import leo14.*

data class Definer(
	val dictionary: Dictionary,
	val script: Script
)

fun Dictionary.definer(script: Script = script()) =
	Definer(this, script)

fun Dictionary.define(script: Script): Dictionary =
	definer().fold(script.lineSeq.reverse) { plus(it) }.dictionary

fun Definer.plus(scriptLine: ScriptLine): Definer =
	when (scriptLine) {
		is FieldScriptLine -> plus(scriptLine.field)
		is LiteralScriptLine -> scriptPlus(scriptLine)
	}

fun Definer.scriptPlus(scriptLine: ScriptLine): Definer =
	dictionary.definer(script.plus(scriptLine))

fun Definer.plus(scriptField: ScriptField): Definer =
	null
		?: plusSpecialOrNull(scriptField)
		?: scriptPlus(line(scriptField))

fun Definer.plusSpecialOrNull(scriptField: ScriptField): Definer? =
	when (scriptField.string) {
		doName -> plusGives(scriptField.rhs)
		becomeName -> plusBecome(scriptField.rhs)
		else -> null
	}

fun Definer.plusGives(rhs: Script): Definer =
	dictionary
		.plus(script, binding(dictionary.function(body(rhs))))
		.definer()

fun Definer.plusBecome(rhs: Script): Definer? =
	dictionary
		.plus(script, binding(dictionary.value(rhs)))
		.definer()
