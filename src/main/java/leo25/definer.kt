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
		beName -> plusBe(scriptField.rhs)
		else -> null
	}

fun Definer.plusGives(rhs: Script): Definer =
	dictionary
		.plus(this.script, binding(dictionary.function(body(rhs))))
		.definer()

fun Definer.plusBe(rhs: Script): Definer? =
	dictionary
		.plus(script("get" lineTo script), binding(dictionary.value(rhs)))
		.definer()
