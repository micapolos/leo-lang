package leo32.runtime

sealed class Op

data class GetOp(
	val get: Get): Op()

data class FieldOp(
	val field: FunctionField): Op()

data class SwitchOp(
	val switch: Switch): Op()

data class CallOp(
	val call: Call): Op()

object EqualsOp: Op()

fun op(get: Get) =
	GetOp(get) as Op

fun op(field: FunctionField) =
	FieldOp(field) as Op

fun op(switch: Switch) =
	SwitchOp(switch)

fun op(call: Call) =
	CallOp(call) as Op

infix fun String.op(function: Function) =
	op(to(function))

fun Op.invoke(term: Term, parameter: Parameter): Term =
	when (this) {
		is GetOp -> get.invoke(term)
		is FieldOp -> field.invoke(term, parameter)
		is SwitchOp -> switch.invoke(term)
		is CallOp -> call.invoke(term)
		is EqualsOp -> term(termField(term == parameter.term))
	}
