package leo25

import leo.base.*
import leo13.mapOrNull
import leo13.seq
import leo14.*
import leo25.parser.scriptOrNull
import leo25.parser.scriptOrThrow
import java.io.File

data class Interpreter(
	val context: Context,
	val value: Value
)

fun Context.interpreter(value: Value = value()) =
	Interpreter(this, value)

fun Interpreter.setLeo(value: Value): Leo<Interpreter> =
	context.interpreter(value).leo

fun Interpreter.set(context: Context): Interpreter =
	context.interpreter(value)

fun Resolver.valueLeo(script: Script): Leo<Value> =
	context.interpreterLeo(script).map { it.value }

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

val Script.resolver: Resolver
	get() =
		nativeResolver.context.interpreter().plusLeo(this).get.context.publicResolver

val String.resolver: Resolver
	get() =
		scriptOrThrow.resolver

val Script.interpretLeo: Leo<Script>
	get() =
		nativeResolver.valueLeo(this).map { it.script }

val String.interpret: String
	get() =
		scriptOrNull?.run { interpret }.orIfNull { value(syntaxName).errorValue.script }.string

fun Context.interpreterLeo(script: Script): Leo<Interpreter> =
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
	}.catch { throwable ->
		setLeo(throwable.causeStackTrace(value.plus(scriptLine.field)))
	}

fun Interpreter.plusLeo(scriptField: ScriptField): Leo<Interpreter> =
	plusDefinitionsOrNullLeo(scriptField).or {
		plusStaticOrNullLeo(scriptField).or {
			plusDynamicLeo(scriptField)
		}
	}

fun Interpreter.plusDefinitionsOrNullLeo(scriptField: ScriptField): Leo<Interpreter?> =
	resolver.definitionSeqOrNullLeo(scriptField).nullableMap { definitionSeq ->
		set(context.fold(definitionSeq.reverse) { plus(it) })
	}

fun Interpreter.plusStaticOrNullLeo(scriptField: ScriptField): Leo<Interpreter?> =
	when (scriptField.string) {
		commentName -> leo(this)
		becomeName -> plusBeLeo(scriptField.rhs)
		doName -> plusDoLeo(scriptField.rhs)
		doingName -> plusDoingLeo(scriptField.rhs)
		evaluateName -> plusEvaluateOrNullLeo(scriptField.rhs)
		getName -> plusGetLeo(scriptField.rhs)
		matchingName -> plusMatchesLeo(scriptField.rhs)
		privateName -> plusPrivateLeo(scriptField.rhs)
		scriptName -> plusScriptLeo(scriptField.rhs)
		switchName -> plusSwitchOrNullLeo(scriptField.rhs)
		traceName -> plusTraceOrNullLeo(scriptField.rhs)
		useName -> plusUseOrNullLeo(scriptField.rhs)
		else -> leo(null)
	}

fun Interpreter.plusBeLeo(rhs: Script): Leo<Interpreter> =
	resolver.valueLeo(rhs).bind { setLeo(it) }

fun Interpreter.plusDoLeo(rhs: Script): Leo<Interpreter> =
	resolver.applyLeo(block(rhs), value).bind { setLeo(it) }

fun Interpreter.plusEvaluateOrNullLeo(rhs: Script): Leo<Interpreter?> =
	notNullIf(rhs.isEmpty) {
		resolver.valueLeo(value.script).bind { setLeo(it) }
	} ?: leo(null)

fun Interpreter.plusDoingLeo(rhs: Script): Leo<Interpreter> =
	plusLeo(field(resolver.function(body(rhs))))

fun Interpreter.plusGetLeo(rhs: Script): Leo<Interpreter> =
	resolver.linesValueLeo(rhs).bind {
		plusLeo(getName fieldTo it)
	}

fun Interpreter.plusScriptLeo(rhs: Script): Leo<Interpreter> =
	setLeo(rhs.value)

fun Interpreter.plusSwitchOrNullLeo(rhs: Script): Leo<Interpreter?> =
	resolver.switchOrNullLeo(value, rhs).nullableBind {
		setLeo(it)
	}

fun Interpreter.plusTraceOrNullLeo(rhs: Script): Leo<Interpreter?> =
	rhs
		.matchEmpty { traceValueLeo.bind { setLeo(it) } }
		?: leo(null)

fun Interpreter.plusDynamicLeo(scriptField: ScriptField): Leo<Interpreter> =
	resolver.fieldLeo(scriptField).bind {
		plusLeo(it)
	}

fun Interpreter.plusLeo(literal: Literal): Leo<Interpreter> =
	plusLeo(field(literal))

fun Interpreter.plusLeo(field: Field): Leo<Interpreter> =
	resolver.resolveLeo(value.plus(field)).bind {
		setLeo(it)
	}

fun Interpreter.plusMatchesLeo(rhs: Script): Leo<Interpreter> =
	setLeo(value.matching(pattern(rhs)))

fun Interpreter.plusPrivateLeo(rhs: Script): Leo<Interpreter> =
	context.private.interpreterLeo(rhs).map { interpreter ->
		use(interpreter.context.publicResolver)
	}

fun Interpreter.plusUseOrNullLeo(rhs: Script): Leo<Interpreter?> =
	rhs
		.lineSeq
		.stack
		.mapOrNull { literalOrNull?.stringOrNull }
		?.let { strings ->
			leo.fold(strings.seq.reverse) { string ->
				bind { interpreter ->
					interpreter.useLeo(string)
				}
			}
		}
		?: leo(null)

val Interpreter.resolver
	get() =
		context.privateResolver

fun Interpreter.useLeo(string: String): Leo<Interpreter> =
	Leo { it.libraryEffect(File(string)) }.map { use(it) }

fun Interpreter.use(resolver: Resolver): Interpreter =
	set(context.plusPrivate(resolver))