package leo21.compiled

import leo.base.fold
import leo.base.reverse
import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.isEmpty
import leo14.lineSeq
import leo21.typed.LineTyped
import leo21.typed.Typed
import leo21.typed.getOrNull
import leo21.typed.lineTo
import leo21.typed.lineTyped
import leo21.typed.plus
import leo21.typed.typed

data class Compiled(
	val scope: Scope,
	val body: Typed,
	val isEmpty: Boolean
)

fun Scope.typed(script: Script): Typed =
	Compiled(this, typed(), isEmpty = true).plus(script).typed

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
		"do" -> TODO()
		"function" -> TODO()
		"make" -> TODO()
		else -> null
	}

fun Compiled.plusNonKeyword(scriptField: ScriptField): Compiled =
	if (scriptField.rhs.isEmpty) plusOrNull(scriptField.string)!!
	else resolvePlus(scriptField).resolve

fun Compiled.resolvePlus(scriptField: ScriptField): Compiled =
	plus(scriptField.string lineTo scope.typed(scriptField.rhs))

fun Compiled.plusOrNull(name: String): Compiled? =
	if (isEmpty) scope.resolveOrNull(name)?.let { setBody(it) }
	else body.getOrNull(name)?.let { setBody(it) }

val Compiled.resolve
	get() =
		setBody(scope.resolve(body))

fun Compiled.plus(typed: LineTyped): Compiled =
	setBody(scope.resolve(body.plus(typed)))

fun Compiled.setBody(body: Typed) = copy(body = body, isEmpty = false)
val Compiled.typed: Typed get() = body.push(scope)