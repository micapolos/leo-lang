package leo20

import leo.base.notNullIf
import leo13.Stack
import leo13.mapFirst
import leo13.push
import leo13.stack
import leo14.Script
import leo14.fieldOrNull
import leo14.onlyLineOrNull

data class Bindings(val definitionStack: Stack<Definition>)

val emptyBindings = Bindings(stack())
fun Bindings.push(definition: Definition): Bindings = Bindings(definitionStack.push(definition))

fun Bindings.resolveOrNull(param: Value): Value? =
	definitionStack.mapFirst { resolveOrNull(param) }

fun Bindings.resolve(param: Value): Value =
	resolveOrNull(param) ?: param

val Bindings.pushPrelude
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
