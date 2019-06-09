package leo5.core

sealed class Function

data class ArgumentFunction(val argument: Argument) : Function()
data class ValueFunction(val value: Value) : Function()
data class PairFunction(val pair: FunctionPair) : Function()
data class AtFunction(val at: At) : Function()
data class BranchFunction(val branch: Branch) : Function()
data class CallFunction(val call: Call) : Function()

fun function(argument: Argument): Function = ArgumentFunction(argument)
fun function(value: Value): Function = ValueFunction(value)
fun function(pair: FunctionPair): Function = PairFunction(pair)
fun function(at: At): Function = AtFunction(at)
fun function(branch: Branch): Function = BranchFunction(branch)
fun function(call: Call): Function = CallFunction(call)

fun Function.invoke(parameter: Value): Value = when (this) {
	is ArgumentFunction -> parameter
	is ValueFunction -> value
	is PairFunction -> pair.invoke(parameter)
	is AtFunction -> at.invoke(parameter)
	is BranchFunction -> branch.invoke(parameter)
	is CallFunction -> call.invoke(parameter)
}