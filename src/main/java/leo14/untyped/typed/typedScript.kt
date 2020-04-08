@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo14.*
import leo14.Number
import leo14.lambda.runtime.Value
import leo14.untyped.nativeName
import leo14.fieldTo as scriptFieldTo

fun Scope.script(typed: Typed): Script =
	script(typed.type, typed.value, null)

fun Scope.script(type: Type, value: Value, recursiveOrNull: TypeRecursive?): Script =
	when (type) {
		EmptyType -> script()
		is LinkType -> script(type.link, value, recursiveOrNull)
		is AlternativeType -> script(type.alternative, value, recursiveOrNull)
		is FunctionType -> script(type.function, value)
		is RepeatingType -> script(type.repeating, value, recursiveOrNull)
		is RecursiveType -> script(type.recursive.type, value, type.recursive)
		RecurseType -> script(recursiveOrNull!!.type, value, recursiveOrNull)
		AnythingType -> script(value as Typed)
		NothingType -> null!!
	}

fun Scope.script(typeLink: TypeLink, value: Value, recursiveOrNull: TypeRecursive?): Script =
	script(scriptLink(typeLink, value, recursiveOrNull))

fun Scope.scriptLink(typeLink: TypeLink, value: Value, recursiveOrNull: TypeRecursive?): ScriptLink =
	if (typeLink.lhs.isStatic || typeLink.line.isStatic)
		script(typeLink.lhs, value, recursiveOrNull) linkTo scriptLine(typeLink.line, value, recursiveOrNull)
	else (value as Pair<*, *>).let { (first, second) ->
		script(typeLink.lhs, first, recursiveOrNull) linkTo scriptLine(typeLink.line, second, recursiveOrNull)
	}

fun Scope.scriptLine(line: TypeLine, value: Value, recursiveOrNull: TypeRecursive?): ScriptLine =
	when (line) {
		is LiteralTypeLine -> line(line.literal)
		is FieldTypeLine -> scriptLine(line.field, value, recursiveOrNull)
		NativeTypeLine -> nativeScriptLine(value)
		NumberTypeLine -> numberScriptLine(value)
		TextTypeLine -> textScriptLine(value)
	}

fun Scope.script(alternative: TypeAlternative, value: Value, recursiveOrNull: TypeRecursive?): Script =
	if (alternative.lhs.isStatic && alternative.rhs.isStatic)
		script(
			if (value as Boolean) alternative.rhs else alternative.lhs,
			null,
			recursiveOrNull)
	else (value as Selected).let { selector ->
		script(
			if (selector.isRhs) alternative.rhs else alternative.lhs,
			selector.value,
			recursiveOrNull)
	}

fun script(function: TypeFunction, value: Value): Script =
	(value as (Value) -> Value).let {
		function.script
	}

fun Scope.script(repeating: TypeRepeating, value: Value, recursiveOrNull: TypeRecursive?): Script =
	if (value == null) script()
	else (value as Pair<*, *>).let { pair ->
		script(repeating, pair.first, recursiveOrNull)
			.plus(script(repeating.type, pair.second, recursiveOrNull))
	}

fun nativeScriptLine(value: Value): ScriptLine =
	nativeName lineTo script(literal(value.toString()))

fun numberScriptLine(value: Value): ScriptLine =
	line(literal(value as Number))

fun textScriptLine(value: Value): ScriptLine =
	line(literal(value as String))

fun Scope.scriptLine(field: TypeField, value: Value, recursiveOrNull: TypeRecursive?): ScriptLine =
	line(scriptField(field, value, recursiveOrNull))

fun Scope.scriptField(field: TypeField, value: Value, recursiveOrNull: TypeRecursive?): ScriptField =
	field.name scriptFieldTo script(field.rhs, value, recursiveOrNull)
