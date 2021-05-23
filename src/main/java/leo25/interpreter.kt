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

fun Dictionary.linesValue(script: Script): Value =
	value().fold(script.lineSeq.reverse) { plus(field(it)) }

fun Dictionary.field(scriptLine: ScriptLine): Field =
	when (scriptLine) {
		is FieldScriptLine -> field(scriptLine.field)
		is LiteralScriptLine -> field(scriptLine.literal)
	}

fun Dictionary.field(scriptField: ScriptField): Field =
	scriptField.string fieldTo value(scriptField.rhs)

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
		becomeName -> plusBe(scriptField.rhs)
		doName -> plusDo(scriptField.rhs)
		doingName -> plusDoing(scriptField.rhs)
		evaluateName -> plusEvaluateOrNull(scriptField.rhs)
		letName -> plusLetOrNull(scriptField.rhs)
		getName -> plusGet(scriptField.rhs)
		scriptName -> plusScript(scriptField.rhs)
		setName -> plusSetOrNull(scriptField.rhs)
		switchName -> plusSwitchOrNull(scriptField.rhs)
		else -> null
	}

fun Interpreter.plusBe(rhs: Script): Interpreter =
	set(dictionary.value(rhs))

fun Interpreter.plusDo(rhs: Script): Interpreter =
	set(dictionary.apply(block(rhs), value))

fun Interpreter.plusEvaluateOrNull(rhs: Script): Interpreter? =
	notNullIf(rhs.isEmpty) {
		set(dictionary.value(value.script))
	}

fun Interpreter.plusDoing(rhs: Script): Interpreter =
	plus(field(dictionary.function(body(rhs))))

fun Interpreter.plusGet(rhs: Script): Interpreter =
	plus(getName fieldTo dictionary.linesValue(rhs))

fun Interpreter.plusLetOrNull(rhs: Script): Interpreter? =
	rhs.matchInfix(doName) { lhs, rhs ->
		set(dictionary.plus(lhs, body(rhs)))
	}

fun Interpreter.plusScript(rhs: Script): Interpreter? =
	set(rhs.value)

fun Interpreter.plusSetOrNull(rhs: Script): Interpreter? =
	notNullIf(value.isEmpty) {
		set(dictionary.set(dictionary.value(rhs)))
	}

fun Interpreter.plusSwitchOrNull(rhs: Script): Interpreter? =
	dictionary.switchOrNull(value, rhs)?.let { set(it) }

fun Interpreter.plusDynamic(scriptField: ScriptField): Interpreter =
	plus(dictionary.field(scriptField))

fun Interpreter.plus(literal: Literal): Interpreter =
	plus(field(literal))

fun Interpreter.plus(line: Field): Interpreter =
	set(dictionary.resolve(value.plus(line)))
