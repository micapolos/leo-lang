@file:Suppress("unused")

package leo32.runtime

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Function

data class EmptyFunction(
	val empty: Empty): Function()

data class ArgumentFunction(
	val argument: Argument): Function()

data class ApplicationFunction(
	val application: Application): Function()

val Empty.function get() =
	EmptyFunction(this) as Function

val Argument.function get() =
	ArgumentFunction(this) as Function

val Application.function get() =
	ApplicationFunction(this) as Function

fun Function.plus(op: Op) =
	application(op).function

fun Function.plus(field: FunctionField) =
	plus(field.op)

fun Function.plus(name: String) =
	plus(name.fieldToEmptyFunction)

fun function(vararg ops: Op) =
	empty.function.fold(ops) { plus(it) }

fun function(argument: Argument, vararg ops: Op) =
	argument.function.fold(ops) { plus(it) }

fun function(string: String) =
	empty.function.plus(string)

fun Function.invoke(parameter: Parameter): Term =
	when (this) {
		is EmptyFunction -> empty.term
		is ArgumentFunction -> argument.invoke(parameter)
		is ApplicationFunction -> application.invoke(parameter)
	}
