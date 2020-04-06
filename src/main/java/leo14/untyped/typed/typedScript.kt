package leo14.untyped.typed

import leo14.*
import leo14.lambda.runtime.Value
import leo14.untyped.nativeName
import leo14.fieldTo as scriptFieldTo

fun Scope.script(type: Type, value: Value): Script =
	when (type) {
		is EmptyType -> script()
		is LinkType -> script(scriptLink(type.link, value))
	}

fun Scope.scriptLink(typeLink: TypeLink, value: Value): ScriptLink =
	if (!typeLink.lhs.isStatic && !typeLink.choice.isStatic)
		(value as Pair<*, *>).let { (first, second) ->
			script(typeLink.lhs, first) linkTo scriptLine(typeLink.choice, second)
		}
	else
		script(typeLink.lhs, value) linkTo scriptLine(typeLink.choice, value)

fun Scope.scriptLine(choice: Choice, value: Value): ScriptLine =
	when (choice) {
		is EmptyChoice -> error("impossible")
		is LinkChoice -> scriptLine(choice.link, value)
	}

fun Scope.scriptLine(choiceLink: ChoiceLink, value: Value): ScriptLine =
	if (choiceLink.lhs is EmptyChoice) scriptLine(choiceLink.line, value)
	else (value as IndexedValue<*>).let { indexedValue ->
		scriptLine(choiceLink, indexedValue.index, indexedValue.value)
	}

fun Scope.scriptLine(choiceLink: ChoiceLink, index: Int, value: Value): ScriptLine =
	if (index == 0) scriptLine(choiceLink.line, value)
	else scriptLine((choiceLink.lhs as LinkChoice).link, index.dec(), value)

fun Scope.scriptLine(line: TypeLine, value: Value): ScriptLine =
	when (line) {
		is LiteralTypeLine -> scriptLine(line.literal, value)
		is NativeTypeLine -> scriptLine(line.native, value)
		is FieldTypeLine -> line(scriptField(line.field, value))
	}

fun Scope.scriptLine(literal: Literal, value: Value): ScriptLine =
	line(literal)

fun Scope.scriptLine(native: Native, value: Value): ScriptLine =
	nativeName lineTo script(literal(value.toString()))

fun Scope.scriptField(field: TypeField, value: Value): ScriptField =
	field.name scriptFieldTo script(field.rhs, value)