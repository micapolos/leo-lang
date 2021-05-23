package leo25

import leo.base.fold
import leo.base.notNullIf
import leo.base.reverse
import leo14.*
import leo25.parser.scriptOrNull

data class Interpreter(
	val resolver: Resolver,
	val value: Value
)

fun Resolver.interpreter(value: Value = value()) =
	Interpreter(this, value)

fun Interpreter.set(value: Value): Interpreter =
	resolver.interpreter(value)

fun Interpreter.set(resolver: Resolver): Interpreter =
	resolver.interpreter(value)

fun Resolver.value(script: Script): Value =
	interpreter(script).value

fun Resolver.linesValue(script: Script): Value =
	value().fold(script.lineSeq.reverse) { plus(field(it)) }

fun Resolver.field(scriptLine: ScriptLine): Field =
	when (scriptLine) {
		is FieldScriptLine -> field(scriptLine.field)
		is LiteralScriptLine -> field(scriptLine.literal)
	}

fun Resolver.field(scriptField: ScriptField): Field =
	scriptField.string fieldTo value(scriptField.rhs)

val Script.interpret: Script
	get() =
		nativeResolver.value(this).script

val String.interpret: String
	get() =
		scriptOrNull?.interpret?.string ?: this

fun Resolver.interpreter(script: Script): Interpreter =
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
		?: plusDefinitionsOrNull(scriptField)
		?: plusStaticOrNull(scriptField)
		?: plusDynamic(scriptField)

fun Interpreter.plusDefinitionsOrNull(scriptField: ScriptField): Interpreter? =
	resolver.plusOrNull(scriptField)?.let { set(it) }

fun Interpreter.plusStaticOrNull(scriptField: ScriptField): Interpreter? =
	when (scriptField.string) {
		commentName -> this
		becomeName -> plusBe(scriptField.rhs)
		doName -> plusDo(scriptField.rhs)
		doingName -> plusDoing(scriptField.rhs)
		evaluateName -> plusEvaluateOrNull(scriptField.rhs)
		getName -> plusGet(scriptField.rhs)
		scriptName -> plusScript(scriptField.rhs)
		switchName -> plusSwitchOrNull(scriptField.rhs)
		else -> null
	}

fun Interpreter.plusBe(rhs: Script): Interpreter =
	set(resolver.value(rhs))

fun Interpreter.plusDo(rhs: Script): Interpreter =
	set(resolver.apply(block(rhs), value))

fun Interpreter.plusEvaluateOrNull(rhs: Script): Interpreter? =
	notNullIf(rhs.isEmpty) {
		set(resolver.value(value.script))
	}

fun Interpreter.plusDoing(rhs: Script): Interpreter =
	plus(field(resolver.function(body(rhs))))

fun Interpreter.plusGet(rhs: Script): Interpreter =
	plus(getName fieldTo resolver.linesValue(rhs))

fun Interpreter.plusLetOrNull(rhs: Script): Interpreter? =
	rhs.matchInfix(doName) { lhs, rhs ->
		set(resolver.plus(lhs, body(rhs)))
	}

fun Interpreter.plusScript(rhs: Script): Interpreter? =
	set(rhs.value)

fun Interpreter.plusSetOrNull(rhs: Script): Interpreter? =
	notNullIf(value.isEmpty) {
		set(resolver.set(resolver.value(rhs)))
	}

fun Interpreter.plusSwitchOrNull(rhs: Script): Interpreter? =
	resolver.switchOrNull(value, rhs)?.let { set(it) }

fun Interpreter.plusDynamic(scriptField: ScriptField): Interpreter =
	plus(resolver.field(scriptField))

fun Interpreter.plus(literal: Literal): Interpreter =
	plus(field(literal))

fun Interpreter.plus(line: Field): Interpreter =
	set(resolver.resolve(value.plus(line)))
