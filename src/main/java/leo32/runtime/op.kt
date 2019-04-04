package leo32.runtime

import leo.base.empty

sealed class Op

data class GetOp(
	val getter: Getter): Op()

data class FieldOp(
	val field: FunctionField): Op()

data class SwitchOp(
	val switch: Switch): Op()

data class InvokeOp(
	val call: Call): Op()

val Getter.op get() =
	GetOp(this) as Op

val FunctionField.op get() =
	FieldOp(this) as Op

fun op(switch: Switch) =
	SwitchOp(switch)

fun op(call: Call) =
	InvokeOp(call) as Op

infix fun String.op(function: Function) =
	fieldTo(function).op

val String.op get() =
	op(empty.function)

fun Op.invoke(term: Term, parameter: Parameter): Term =
	when (this) {
		is GetOp -> getter.invoke(term)
		is FieldOp -> field.invoke(term, parameter)
		is SwitchOp -> switch.invoke(term)
		is InvokeOp -> call.invoke(term)
	}
