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

fun Resolver.valueLeo(script: Script): Leo<Value> =
	interpreterLeo(script).map { it.value }

fun Resolver.linesValueLeo(script: Script): Leo<Value> =
	value().leo.fold(script.lineSeq.reverse) { line ->
		bind { value ->
			fieldLeo(line).map {
				value.plus(it)
			}
		}
	}

fun Resolver.fieldLeo(scriptLine: ScriptLine): Leo<Field> =
	when (scriptLine) {
		is FieldScriptLine -> fieldLeo(scriptLine.field)
		is LiteralScriptLine -> field(scriptLine.literal).leo
	}

fun Resolver.fieldLeo(scriptField: ScriptField): Leo<Field> =
	valueLeo(scriptField.rhs).map {
		scriptField.string fieldTo it
	}

val Script.interpret: Script
	get() =
		interpretLeo.get

val Script.interpretLeo: Leo<Script>
	get() =
		nativeResolver.valueLeo(this).map { it.script }

val String.interpret: String
	get() =
		scriptOrNull?.interpret?.string ?: this

fun Resolver.interpreterLeo(script: Script): Leo<Interpreter> =
	interpreter().plusLeo(script)

fun Interpreter.plusLeo(script: Script): Leo<Interpreter> =
	leo.fold(script.lineSeq.reverse) { line ->
		bind {
			it.plusLeo(line)
		}
	}

fun Interpreter.plusLeo(scriptLine: ScriptLine): Leo<Interpreter> =
	when (scriptLine) {
		is FieldScriptLine -> plusLeo(scriptLine.field)
		is LiteralScriptLine -> plusLeo(scriptLine.literal)
	}

fun Interpreter.plusLeo(scriptField: ScriptField): Leo<Interpreter> =
	plusDefinitionsOrNullLeo(scriptField).or {
		plusStaticOrNullLeo(scriptField).or {
			plusDynamicLeo(scriptField)
		}
	}

fun Interpreter.plusDefinitionsOrNullLeo(scriptField: ScriptField): Leo<Interpreter?> =
	resolver.plusOrNullLeo(scriptField).nullableBind { set(it).leo }

fun Interpreter.plusStaticOrNullLeo(scriptField: ScriptField): Leo<Interpreter?> =
	when (scriptField.string) {
		commentName -> leo(this)
		becomeName -> plusBeLeo(scriptField.rhs)
		doName -> plusDoLeo(scriptField.rhs)
		doingName -> plusDoingLeo(scriptField.rhs)
		evaluateName -> plusEvaluateOrNullLeo(scriptField.rhs)
		getName -> plusGetLeo(scriptField.rhs)
		scriptName -> plusScript(scriptField.rhs).leo
		switchName -> plusSwitchOrNullLeo(scriptField.rhs)
		else -> leo(null)
	}

fun Interpreter.plusBeLeo(rhs: Script): Leo<Interpreter> =
	resolver.valueLeo(rhs).map { set(it) }

fun Interpreter.plusDoLeo(rhs: Script): Leo<Interpreter> =
	resolver.applyLeo(block(rhs), value).map { set(it) }

fun Interpreter.plusEvaluateOrNullLeo(rhs: Script): Leo<Interpreter?> =
	notNullIf(rhs.isEmpty) {
		resolver.valueLeo(value.script).map { set(it) }
	} ?: leo(null)

fun Interpreter.plusDoingLeo(rhs: Script): Leo<Interpreter> =
	plusLeo(field(resolver.function(body(rhs))))

fun Interpreter.plusGetLeo(rhs: Script): Leo<Interpreter> =
	resolver.linesValueLeo(rhs).bind {
		plusLeo(getName fieldTo it)
	}

fun Interpreter.plusScript(rhs: Script): Interpreter? =
	set(rhs.value)

fun Interpreter.plusSwitchOrNullLeo(rhs: Script): Leo<Interpreter?> =
	resolver.switchOrNullLeo(value, rhs).map {
		it?.let { set(it) }
	}

fun Interpreter.plusDynamicLeo(scriptField: ScriptField): Leo<Interpreter> =
	resolver.fieldLeo(scriptField).bind {
		plusLeo(it)
	}

fun Interpreter.plusLeo(literal: Literal): Leo<Interpreter> =
	plusLeo(field(literal))

fun Interpreter.plusLeo(field: Field): Leo<Interpreter> =
	resolver.resolveLeo(value.plus(field)).map {
		set(it)
	}
