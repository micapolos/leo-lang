package leo21.compiled

import leo.base.fold
import leo.base.notNullOrError
import leo.base.reverse
import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lineSeq
import leo14.onlyStringOrNull
import leo21.typed.LineTyped
import leo21.typed.Typed
import leo21.typed.lineTo
import leo21.typed.lineTyped
import leo21.typed.make
import leo21.typed.plus
import leo21.typed.resolveGetOrNull
import leo21.typed.typed

data class Compiled(
	val bindings: Bindings,
	val body: Typed
)

fun Bindings.typed(script: Script): Typed =
	Compiled(this, typed()).plus(script).body

fun Compiled.plus(script: Script): Compiled =
	fold(script.lineSeq.reverse) { plus(it) }

fun Compiled.plus(scriptLine: ScriptLine): Compiled =
	when (scriptLine) {
		is LiteralScriptLine -> plus(scriptLine.literal)
		is FieldScriptLine -> plus(scriptLine.field)
	}

fun Compiled.plus(literal: Literal): Compiled =
	plus(lineTyped(literal)).resolve

fun Compiled.plus(scriptField: ScriptField): Compiled =
	null
		?: plusKeywordOrNull(scriptField)
		?: plusNonKeyword(scriptField)

fun Compiled.plusKeywordOrNull(scriptField: ScriptField): Compiled? =
	when (scriptField.string) {
		"do" -> plusDo(scriptField.rhs)
		"function" -> plusFunction(scriptField.rhs)
		"make" -> plusMake(scriptField.rhs)
		else -> null
	}

fun Compiled.plusDo(script: Script): Compiled =
	bindings.push(body.type).typed(script).let { typed ->
		setBody(
			Typed(
				fn(typed.term).invoke(body.term),
				typed.type))
	}

fun Compiled.plusFunction(script: Script): Compiled =
	setBody(body.plus(lineTyped(bindings.arrowTyped(script))))

fun Compiled.plusMake(script: Script): Compiled =
	setBody(body.make(script.onlyStringOrNull.notNullOrError("make syntax error")))

fun Compiled.plusNonKeyword(scriptField: ScriptField): Compiled =
	resolvePlus(scriptField).resolve

fun Compiled.resolvePlus(scriptField: ScriptField): Compiled =
	plus(scriptField.string lineTo bindings.typed(scriptField.rhs))

val Compiled.resolve
	get() =
		setBody(body.resolveGetOrNull ?: bindings.resolve(body))

fun Compiled.plus(typed: LineTyped): Compiled =
	setBody(bindings.resolve(body.plus(typed)))

fun Compiled.setBody(body: Typed) = copy(body = body)
