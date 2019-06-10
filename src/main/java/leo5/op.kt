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
	is LhsOp -> value.invokeLhs
	is RhsOp -> value.invokeRhs
	is CallOp -> call.body.invoke(value)
	is PlusOp -> value.invokePlus(line(plus.name, plus.body.invoke(argument)))
	is DispatchOp -> value.invokeDispatch(dispatcher, argument)
}
