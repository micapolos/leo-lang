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
	plus(lineTyped(literal))

fun Compiled.plus(scriptField: ScriptField): Compiled =
	if (scriptField.rhs.isEmpty) plusOrNull(scriptField.string)!!
	else plusResolve(scriptField)

fun Compiled.plusResolve(scriptField: ScriptField): Compiled =
	plusResolve(scriptField.string lineTo scope.typed(scriptField.rhs))

fun Compiled.plusResolve(lineTyped: LineTyped): Compiled =
	setBody(scope.resolve(body.plus(lineTyped)))

fun Compiled.plusOrNull(name: String): Compiled? =
	if (isEmpty) scope.resolveOrNull(name)?.let { setBody(it) }
	else body.getOrNull(name)?.let { setBody(it) }

fun Compiled.plus(typed: LineTyped): Compiled =
	setBody(scope.resolve(body.plus(typed)))

fun Compiled.setBody(body: Typed) = copy(body = body, isEmpty = false)
val Compiled.typed: Typed get() = body.push(scope)