package leo25

import leo.base.fold
import leo.base.reverse
import leo14.*
import leo25.parser.scriptOrNull

data class Interpreter(
	val context: Context,
	val value: Value
)

fun Context.interpreter(value: Value = value()) =
	Interpreter(this, value)

fun Context.interpretedValue(script: Script): Value =
	interpreter().fold(script.lineSeq.reverse) { plus(it) }.value

val Script.interpret: Script
	get() =
		context().interpretedValue(this).script

val String.interpret: String get() = scriptOrNull?.interpret?.string ?: this

fun Interpreter.plus(scriptLine: ScriptLine): Interpreter =
	// TODO: Resolve static definitions, function etc...
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
		giveName -> plusGive(scriptField.rhs)
		givingName -> plusGiving(scriptField.rhs)
		letName -> plusLet(scriptField.rhs)
		else -> null
	}

fun Interpreter.plusGive(script: Script): Interpreter =
	context.interpreter(context.plusGiven(value).interpretedValue(script))

fun Interpreter.plusGiving(script: Script): Interpreter =
	plus(line(Function(context, script)))

fun Interpreter.plusLet(script: Script): Interpreter =
	context.define(script).interpreter(value)

fun Interpreter.plusDynamic(scriptField: ScriptField): Interpreter =
	context.interpretedValue(scriptField.rhs).let { valueOrNull ->
		plus(scriptField.string lineTo valueOrNull)
	}

fun Interpreter.plus(literal: Literal): Interpreter =
	plus(line(literal))

fun Interpreter.plus(line: Line): Interpreter =
	context.interpreter(context.resolve(value.plus(line)))
