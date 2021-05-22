package leo25

import leo.base.fold
import leo.base.notNullIf
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
	interpreter(script).value

val Script.interpret: Script
	get() =
		context().interpretedValue(this).script

val String.interpret: String get() = scriptOrNull?.interpret?.string ?: this

fun Context.interpreter(script: Script): Interpreter =
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
		doName -> plusDo(scriptField.rhs)
		doingName -> plusGiving(scriptField.rhs)
		evaluateName -> plusEvaluateOrNull(scriptField.rhs)
		letName -> plusLet(scriptField.rhs)
		switchName -> plusSwitchOrNull(scriptField.rhs)
		else -> null
	}

fun Interpreter.plusDo(rhs: Script): Interpreter =
	context.interpreter(context.apply(block(rhs), value))

fun Interpreter.plusEvaluateOrNull(rhs: Script): Interpreter? =
	notNullIf(rhs.isEmpty) {
		context.interpreter(context.interpretedValue(rhs))
	}

fun Interpreter.plusGiving(rhs: Script): Interpreter =
	plus(line(context.function(body(rhs))))

fun Interpreter.plusLet(rhs: Script): Interpreter =
	context.define(rhs).interpreter(value)

fun Interpreter.plusSwitchOrNull(rhs: Script): Interpreter? =
	context.switchOrNull(value, rhs)?.let { context.interpreter(it) }

fun Interpreter.plusDynamic(scriptField: ScriptField): Interpreter =
	plus(scriptField.string lineTo context.interpretedValue(scriptField.rhs))

fun Interpreter.plus(literal: Literal): Interpreter =
	plus(line(literal))

fun Interpreter.plus(line: Line): Interpreter =
	context.interpreter(context.resolve(value.plus(line)))
