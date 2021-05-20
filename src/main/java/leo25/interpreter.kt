package leo25

import leo.base.fold
import leo.base.reverse
import leo14.*

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
		functionName -> plus(line(Function(context, scriptField.rhs)))
		defineName -> context.define(scriptField.rhs).interpreter(value)
		else -> null
	}

fun Interpreter.plusDynamic(scriptField: ScriptField): Interpreter =
	context.interpretedValue(scriptField.rhs).let { valueOrNull ->
		plus(scriptField.string lineTo valueOrNull)
	}

fun Interpreter.plus(literal: Literal): Interpreter =
	plus(line(literal))

fun Interpreter.plus(line: Line): Interpreter =
	context.interpreter(context.resolve(value.plus(line)))
