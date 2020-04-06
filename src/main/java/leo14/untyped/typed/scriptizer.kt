package leo14.untyped.typed

import leo.base.empty
import leo13.metaName
import leo14.*
import leo14.Number
import leo14.lambda.runtime.Value
import leo14.line
import leo14.untyped.*
import leo14.fieldTo as scriptFieldTo

data class Scriptizer(
	val scope: Scope,
	val recurseEvaluatedOrNull: Evaluated?)

val Scope.scriptizer get() = Scriptizer(this, null)
val emptyScriptizer = empty.scope.scriptizer

fun Scriptizer.script(evaluated: Evaluated): Script =
	script(evaluated.typeScript, evaluated.value)

fun Scriptizer.script(typeScript: Script, value: Value): Script =
	when (typeScript) {
		is UnitScript -> script()
		is LinkScript -> script(typeScript.link, value)
	}

fun Scriptizer.script(typeScriptLink: ScriptLink, value: Value): Script =
	typeScriptLink.typeStaticScriptOrNull ?: script(scriptLink(typeScriptLink, value))

fun Scriptizer.scriptLink(typeLink: ScriptLink, value: Value): ScriptLink =
	if (typeLink.lhs.isEmpty) link(scriptLine(typeLink.line, value))
	else (value as Pair<*, *>).let { (lhsValue, rhsValue) ->
		script(typeLink.lhs, lhsValue) linkTo scriptLine(typeLink.line, rhsValue)
	}

fun Scriptizer.scriptLine(typeLine: ScriptLine, value: Value): ScriptLine =
	when (typeLine) {
		is LiteralScriptLine -> typeLine
		is FieldScriptLine -> scriptLine(typeLine.field, value)
	}

fun Scriptizer.scriptLine(typeField: ScriptField, value: Value): ScriptLine =
	when (typeField.string) {
		textName -> line(literal(value as String))
		numberName -> line(literal(value as Number))
		nativeName -> nativeName lineTo script(literal(value.toString()))
		eitherName -> eitherScriptLine(typeField.rhs, value)
		metaName -> TODO()
		else -> line(scriptField(typeField, value))
	}

fun Scriptizer.scriptField(typeField: ScriptField, value: Value): ScriptField =
	typeField.string scriptFieldTo script(typeField.rhs, value)

fun Scriptizer.eitherScriptLine(eitherScript: Script, value: Value): ScriptLine =
	(value as IndexedValue<*>).let { (index, value) ->
		eitherScriptLine(eitherScript, index, value)
	}

fun Scriptizer.eitherScriptLine(eitherScript: Script, index: Int, value: Value): ScriptLine =
	when (eitherScript) {
		is UnitScript -> null!!
		is LinkScript -> eitherScriptLine(eitherScript.link, index, value)
	}

fun Scriptizer.eitherScriptLine(eitherScriptLink: ScriptLink, index: Int, value: Value): ScriptLine =
	if (index == 0) scriptLine(eitherScriptLink.line, value)
	else eitherScriptLine(eitherScriptLink.lhs, index.dec(), value)

val ScriptLink.typeStaticScriptOrNull: Script?
	get() =
		if (lhs.isEmpty && line is FieldScriptLine && line.field.string == staticName) line.field.rhs
		else null
