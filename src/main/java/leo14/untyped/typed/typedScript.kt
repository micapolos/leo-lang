@file:Suppress("UNCHECKED_CAST")

package leo14.untyped.typed

import leo14.*
import leo14.Number
import leo14.lambda.runtime.Value
import leo14.untyped.nativeName
import leo14.fieldTo as scriptFieldTo

fun Scope.script(type: Type, value: Value, recursiveOrNull: TypeRecursive?): Script =
	when (type) {
		is StaticType -> type.static.script
		is LinkType -> script(type.link, value, recursiveOrNull)
		is FunctionType -> script(type.function, value)
		is RecursiveType -> script(type.recursive.type, value, type.recursive)
		RecurseType -> script(recursiveOrNull!!.type, value, recursiveOrNull)
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
		is FieldTypeLine -> scriptLine(line.field, value, recursiveOrNull)
		is EnumTypeLine -> scriptLine(line.enum, value, recursiveOrNull)
		is ChoiceTypeLine -> scriptLine(line.choice, value, recursiveOrNull)
		NativeTypeLine -> nativeScriptLine(value)
		NumberTypeLine -> numberScriptLine(value)
		TextTypeLine -> textScriptLine(value)
	}

fun script(function: TypeFunction, value: Value): Script =
	(value as (Value) -> Value).let {
		function.script
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

fun Scope.scriptLine(choice: Choice, value: Value, recursiveOrNull: TypeRecursive?): ScriptLine =
	(value as IndexedValue<*>).let { (index, value) ->
		scriptLine(choice, index, value, recursiveOrNull)
	}

fun Scope.scriptLine(choice: Choice, index: Int, value: Value, recursiveOrNull: TypeRecursive?): ScriptLine =
	when (choice) {
		EmptyChoice -> null!!
		is LinkChoice -> scriptLine(choice.link, index, value, recursiveOrNull)
	}

fun Scope.scriptLine(choiceLink: ChoiceLink, index: Int, value: Value, recursiveOrNull: TypeRecursive?): ScriptLine =
	if (index == 0) scriptLine(choiceLink.line, value, recursiveOrNull)
	else scriptLine(choiceLink.lhs, index.dec(), value, recursiveOrNull)

fun Scope.scriptLine(enum: Enum, value: Value, recursiveOrNull: TypeRecursive?): ScriptLine =
	scriptLine(enum, value as Int, recursiveOrNull)

fun Scope.scriptLine(enum: Enum, index: Int, recursiveOrNull: TypeRecursive?): ScriptLine =
	when (enum) {
		EmptyEnum -> null!!
		is LinkEnum -> scriptLine(enum.link, index, recursiveOrNull)
	}

fun Scope.scriptLine(enumLink: EnumLink, index: Int, recursiveOrNull: TypeRecursive?): ScriptLine =
	if (index == 0) enumLink.scriptLine
	else scriptLine(enumLink.lhs, index.dec(), recursiveOrNull)

