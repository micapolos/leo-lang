package leo5.function

sealed class Function
data class ArgumentFunction(val argument: Argument) : Function()
data class ConstantFunction(val constant: Constant) : Function()
data class SwitchFunction(val switch: Switch) : Function()

fun function(argument: Argument): Function = ArgumentFunction(argument)
fun function(constant: Constant): Function = ConstantFunction(constant)
fun function(switch: Switch): Function = SwitchFunction(switch)

fun Function.invoke(parameter: Any) = when (this) {
	is ArgumentFunction -> parameter
	is ConstantFunction -> constant.invoke(parameter)
	is SwitchFunction -> switch.invoke
}