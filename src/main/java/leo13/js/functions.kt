package leo13.js

import leo13.*

data class Functions(val stack: Stack<Function>)

fun functions(vararg functions: Function) = Functions(stack(*functions))
fun Functions.plus(function: Function) = Functions(stack.push(function))

fun Functions.indexedAt(lhs: Types): IndexedValue<Function>? =
	stack.firstIndexed { this.parameterTypes == lhs }

fun Functions.at(index: Int): Function? =
	stack.atIndex(index)

val Functions.code
	get() =
		stack
			.toList()
			.mapIndexed { index, function -> function.code(index) }
			.joinToString("\n")
