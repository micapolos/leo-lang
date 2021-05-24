package leo25

import leo.base.*
import leo.java.io.file
import leo14.*
import leo25.parser.scriptOrNull
import leo25.parser.scriptOrThrow

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
		try {
			interpretLeo.get
		} catch (e: Throwable) {
			e.value.script
		}

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
		hashName -> plusHashOrNullLeo(scriptField.rhs)
		equalsName -> plusEqualsLeo(scriptField.rhs)
		asName -> plusAsLeo(scriptField.rhs)
		privateName -> plusPrivateLeo(scriptField.rhs)
		scriptName -> plusScriptLeo(scriptField.rhs)
		switchName -> plusSwitchOrNullLeo(scriptField.rhs)
		repeatName -> plusRepeatOrNullLeo(scriptField.rhs)
		recurseName -> plusRecurseOrNullLeo(scriptField.rhs)
		traceName -> plusTraceOrNullLeo(scriptField.rhs)
		useName -> plusUseOrNullLeo(scriptField.rhs)
		else -> plusNameOrNullLeo(scriptField)
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

fun Interpreter.plusHashOrNullLeo(rhs: Script): Leo<Interpreter?> =
	if (rhs.isEmpty) setLeo(value.hashValue)
	else leo(null)

fun Interpreter.plusEqualsLeo(rhs: Script): Leo<Interpreter?> =
	resolver.valueLeo(rhs).bind {
		setLeo(value.equals(it).equalsValue)
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

fun Interpreter.plusAsLeo(rhs: Script): Leo<Interpreter> =
	setLeo(value.matching(pattern(rhs)))

fun Interpreter.plusPrivateLeo(rhs: Script): Leo<Interpreter> =
	context.private.interpreterLeo(rhs).map { interpreter ->
		use(interpreter.context.publicResolver)
	}

fun Interpreter.plusUseOrNullLeo(rhs: Script): Leo<Interpreter?> =
	rhs.useOrNull.leo.nullableBind {
		plusLeo(it)
	}

fun Interpreter.plusNameOrNullLeo(scriptField: ScriptField): Leo<Interpreter?> =
	scriptField.onlyStringOrNull?.let { name ->
		value.resolveOrNull(name)
	}.leo.nullableBind { setLeo(it) }

fun Interpreter.plusRecurseOrNullLeo(rhs: Script): Leo<Interpreter?> =
	if (rhs.isEmpty) plusDynamicLeo(recurseName fieldTo script())
	else leo(null)

fun Interpreter.plusRepeatOrNullLeo(rhs: Script): Leo<Interpreter?> =
	if (rhs.isEmpty) plusDynamicLeo(repeatName fieldTo script())
	else leo(null)

val Interpreter.resolver
	get() =
		context.privateResolver

fun Interpreter.plusLeo(use: Use): Leo<Interpreter> =
	Leo { it.libraryEffect(use) }.map { use(it) }

fun Interpreter.use(resolver: Resolver): Interpreter =
	set(context.plusPrivate(resolver))