package leo20

import leo.base.notNullIf
import leo13.Stack
import leo13.mapFirst
import leo13.push
import leo13.stack
import leo14.Script
import leo14.fieldOrNull
import leo14.onlyLineOrNull

data class Bindings(val bindingStack: Stack<Binding>)

val emptyBindings = Bindings(stack())
fun Bindings.push(binding: Binding): Bindings = Bindings(bindingStack.push(binding))

fun Bindings.resolveOrNull(param: Value): Value? =
	bindingStack.mapFirst { resolveOrNull(param) }

fun Bindings.resolve(param: Value): Value =
	resolveOrNull(param) ?: param

val Bindings.pushPrelude
	get() = this
		.push(numberPlusBinding)
		.push(numberMinusBinding)
		.push(numberEqualsBinding)
		.push(textAppendBinding)

val Script.recursivelyBodyOrNull: Script?
	get() =
		onlyLineOrNull?.fieldOrNull?.let { field ->
			notNullIf(field.string == "recursively") {
				field.rhs
			}
		}
