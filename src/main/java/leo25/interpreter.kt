package leo25

import leo.base.fold
import leo.base.notNullIf
import leo.base.reverse
import leo14.*
import leo25.parser.scriptOrNull

data class Interpreter(
	val dictionary: Dictionary,
	val value: Value
)

fun Dictionary.interpreter(value: Value = value()) =
	Interpreter(this, value)

fun Interpreter.set(value: Value): Interpreter =
	dictionary.interpreter(value)

fun Interpreter.set(dictionary: Dictionary): Interpreter =
	dictionary.interpreter(value)

fun Dictionary.value(script: Script): Value =
	interpreter(script).value

fun Dictionary.line(scriptField: ScriptField): Line =
	scriptField.string lineTo value(scriptField.rhs)

val Script.interpret: Script
	get() =
		nativeDictionary.value(this).script

val String.interpret: String
	get() =
		scriptOrNull?.interpret?.string ?: this

fun Dictionary.interpreter(script: Script): Interpreter =
	interpreter().plus(script)

fun Interpreter.plus(script: Script): Interpreter =
	fold(script.lineSeq.reverse) { plus(it) }

fun Interpreter.plus(scriptLine: ScriptLine): Interpreter =
	when (scriptLine) {
		is FieldScriptLine -> plus(scriptLine.field)
		is LiteralScriptLine -> plus(scriptLine.literal)
	}

fun Interpreter.plus(scriptField: ScriptField): Interpreter =
	null
		?: plusStaticOrNull(scriptField)
		?: plusDynamic(scriptField)

fun Interpreter.plusStaticOrNull(scriptField: ScriptField): Interpreter? =
	when (scriptField.string) {
		commentName -> this
		doName -> plusDo(scriptField.rhs)
		doingName -> plusGiving(scriptField.rhs)
		evaluateName -> plusEvaluateOrNull(scriptField.rhs)
		letName -> plusLet(scriptField.rhs)
		scriptName -> plusScript(scriptField.rhs)
		switchName -> plusSwitchOrNull(scriptField.rhs)
		else -> null
	}

fun Interpreter.plusDo(rhs: Script): Interpreter =
	set(dictionary.apply(block(rhs), value))

fun Interpreter.plusEvaluateOrNull(rhs: Script): Interpreter? =
	notNullIf(rhs.isEmpty) {
		set(dictionary.value(value.script))
	}

fun Interpreter.plusGiving(rhs: Script): Interpreter =
	plus(line(dictionary.function(body(rhs))))

fun Interpreter.plusLet(rhs: Script): Interpreter =
	set(dictionary.define(rhs))

fun Interpreter.plusScript(rhs: Script): Interpreter? =
	set(rhs.value)

fun Interpreter.plusSwitchOrNull(rhs: Script): Interpreter? =
	dictionary.switchOrNull(value, rhs)?.let { set(it) }

fun Interpreter.plusDynamic(scriptField: ScriptField): Interpreter =
	plus(dictionary.line(scriptField))

fun Interpreter.plus(literal: Literal): Interpreter =
	plus(line(literal))

fun Interpreter.plus(line: Line): Interpreter =
	set(dictionary.resolve(value.plus(line)))
