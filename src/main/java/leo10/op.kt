package leo10

sealed class Op

data class AppendOp(
	val append: OpAppend) : Op()

data class LhsOp(
	val lhs: Lhs) : Op()

data class RhsOp(
	val rhs: Rhs) : Op()

data class SwitchOp(
	val switch: OpSwitch) : Op()

data class CallOp(
	val call: OpCall) : Op()

data class OpAppend(
	val name: String,
	val function: Function)

data class OpSwitch(
	val map: Map<String, Function>)

data class OpCall(
	val function: Function)

fun op(append: OpAppend): Op = AppendOp(append)
fun op(lhs: Lhs): Op = LhsOp(lhs)
fun op(rhs: Rhs): Op = RhsOp(rhs)
fun op(switch: OpSwitch): Op = SwitchOp(switch)
fun op(call: OpCall): Op = CallOp(call)

fun Op.call(target: Script, args: Args) =
	when (this) {
		is AppendOp -> append.call(target, args)
		is LhsOp -> lhs.call(target, args)
		is RhsOp -> rhs.call(target, args)
		is SwitchOp -> switch.call(target, args)
		is CallOp -> call.call(target, args)
	}

fun OpAppend.call(target: Script, args: Args) = target.plus(name lineTo function.call(args))
fun Lhs.call(target: Script, args: Args) = target.lhs
fun Rhs.call(target: Script, args: Args) = target.rhs
fun OpSwitch.call(target: Script, args: Args) = map.getValue(target.name).call(args)
fun OpCall.call(target: Script, args: Args) = function.call(args + target)