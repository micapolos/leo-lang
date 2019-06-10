package leo5

sealed class Op
data class LhsOp(val lhs: Lhs) : Op()
data class RhsOp(val rhs: Rhs) : Op()
data class CallOp(val call: Call) : Op()
data class PlusOp(val plus: Plus) : Op()
data class DispatchOp(val dispatcher: Dispatcher) : Op()

fun op(lhs: Lhs): Op = LhsOp(lhs)
fun op(rhs: Rhs): Op = RhsOp(rhs)
fun op(call: Call): Op = CallOp(call)
fun op(plus: Plus): Op = PlusOp(plus)
fun op(dispatcher: Dispatcher): Op = DispatchOp(dispatcher)

fun Op.invoke(value: Value, argument: Value): Value = when (this) {
	is LhsOp -> value.script.invokeLhs
	is RhsOp -> value.script.invokeRhs
	is CallOp -> value.script.invokeCall(argument)
	is PlusOp -> value.plus(line(plus.name, plus.body.invoke(argument)))
	is DispatchOp -> dispatcher.at(value.script.nameOrNull!!).invoke(argument)
}
