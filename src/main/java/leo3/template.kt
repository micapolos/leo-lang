package leo3

sealed class Template

data class ArgumentTemplate(val argument: Argument) : Template()
data class ValueTemplate(val value: Value) : Template()
data class SelectorTemplate(val selector: Selector) : Template()
data class CallTemplate(val call: Call) : Template()

fun template(argument: Argument): Template = ArgumentTemplate(argument)
fun template(value: Value): Template = ValueTemplate(value)
fun template(selector: Selector): Template = SelectorTemplate(selector)
fun template(call: Call): Template = CallTemplate(call)

fun Template.apply(parameter: Parameter): Value =
	when (this) {
		is ArgumentTemplate -> argument.apply(parameter)
		is ValueTemplate -> value.apply(parameter)
		is SelectorTemplate -> selector.apply(parameter)
		is CallTemplate -> call.apply(parameter)
	}
