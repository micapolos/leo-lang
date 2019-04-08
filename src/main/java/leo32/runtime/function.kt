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
	plus(op(field))

fun Function.plus(name: String) =
	plus(name to function())

fun function(vararg ops: Op) =
	empty.function.fold(ops) { plus(it) }

fun Function.plus(field: TermField): Function =
	plus(field.functionField)

fun function(term: Term): Function =
	function().fold(term.fieldSeq) { plus(it) }

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
