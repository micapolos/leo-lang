package leo25

import leo.base.fold
import leo.base.notNullIf
import leo.base.reverse
import leo14.*
import leo15.comName
import leo25.parser.scriptOrNull

data class Interpreter(
	val context: Context,
	val value: Value
)

fun Context.interpreter(value: Value = value()) =
	Interpreter(this, value)

fun Interpreter.set(value: Value): Interpreter =
	context.interpreter(value)

fun Interpreter.set(context: Context): Interpreter =
	context.interpreter(value)

fun Context.value(script: Script): Value =
	interpreter(script).value

fun Context.line(scriptField: ScriptField): Line =
	scriptField.string lineTo value(scriptField.rhs)

val Script.interpret: Script
	get() =
		nativeContext.value(this).script

val String.interpret: String
	get() =
		scriptOrNull?.interpret?.string ?: this

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
	set(context.apply(block(rhs), value))

fun Interpreter.plusEvaluateOrNull(rhs: Script): Interpreter? =
	notNullIf(rhs.isEmpty) {
		set(context.value(value.script))
	}

fun Interpreter.plusGiving(rhs: Script): Interpreter =
	plus(line(context.function(body(rhs))))

fun Interpreter.plusLet(rhs: Script): Interpreter =
	set(context.define(rhs))

fun Interpreter.plusScript(rhs: Script): Interpreter? =
	set(rhs.value)

fun Interpreter.plusSwitchOrNull(rhs: Script): Interpreter? =
	context.switchOrNull(value, rhs)?.let { set(it) }

fun Interpreter.plusDynamic(scriptField: ScriptField): Interpreter =
	plus(context.line(scriptField))

fun Interpreter.plus(literal: Literal): Interpreter =
	plus(line(literal))

fun Interpreter.plus(line: Line): Interpreter =
	set(context.resolve(value.plus(line)))
