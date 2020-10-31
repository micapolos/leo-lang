package leo20

import leo.base.notNullIf
import leo13.Stack
import leo13.linkOrNull
import leo13.mapFirst
import leo13.push
import leo13.stack
import leo14.Script
import leo14.fieldOrNull
import leo14.isEmpty
import leo14.lineTo
import leo14.linkOrNull
import leo14.onlyLineOrNull
import leo14.plus

data class Dictionary(val definitionStack: Stack<Definition>)

val emptyDictionary = Dictionary(stack())

fun Dictionary.push(definition: Definition): Dictionary =
	Dictionary(definitionStack.push(definition))

fun Dictionary.resolveOrNull(param: Value): Value? =
	definitionStack.mapFirst { resolveOrNull(param) }

fun Dictionary.getOrNull(script: Script): Value? =
	definitionStack.mapFirst { getOrNull(script) }

fun Dictionary.resolve(param: Value): Value =
	resolveOrNull(param) ?: param

val Dictionary.pushPrelude
	get() = this
		.push(numberPlusDefinition)
		.push(numberMinusDefinition)
		.push(numberEqualsDefinition)
		.push(textAppendDefinition)

val Script.recursivelyBodyOrNull: Script?
	get() =
		onlyLineOrNull?.fieldOrNull?.let { field ->
			notNullIf(field.string == "recursively") {
				field.rhs
			}
		}

fun Dictionary.push(value: Value) = push(ValueDefinition(value))

fun Dictionary.defineOrNull(script: Script): Dictionary? =
	script.linkOrNull?.let { link ->
		link.lhs.patternOrNull?.let { pattern ->
			link.line.fieldOrNull?.let { field ->
				when (field.string) {
					"does" -> defineDoes(pattern, field.rhs)
					else -> null
				}
			}
		}
	}

fun Dictionary.defineDoes(pattern: Pattern, script: Script): Dictionary =
	script.recursivelyBodyOrNull
		?.let { recursivelyBody ->
			push(FunctionDefinition(pattern, function(body(recursivelyBody)), isRecursive = true))
		}
		?: push(FunctionDefinition(pattern, function(body(script)), isRecursive = false))

fun Dictionary.test(script: Script) {
	val link = script.linkOrNull ?: error("syntax" lineTo script)
	val field = link.line.fieldOrNull ?: error("syntax" lineTo script)
	when (field.string) {
		"equals" -> {
			val lhsValue = value(link.lhs)
			val rhsValue = value(field.rhs)
			if (lhsValue != rhsValue)
				error(
					"test" lineTo script,
					"result" lineTo lhsValue.script.plus("equals" lineTo rhsValue.script))
		}
		"fails" -> {
			if (!field.rhs.isEmpty) error("syntax" lineTo script)
			val success = try {
				value(link.lhs)
				true
			} catch (exception: Exception) {
				false
			}
			if (success) error("test" lineTo script)
		}
		else -> error("syntax" lineTo script)
	}
}

val Dictionary.unsafeGiven
	get() =
		value("given" lineTo (definitionStack.linkOrNull!!.value as ValueDefinition).value)
