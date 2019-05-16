package leo3

sealed class Template
data class ArgumentTemplate(val argument: Argument) : Template()
data class SelectorTemplate(val selector: Selector) : Template()
data class ApplicationTemplate(val application: Application) : Template()

fun template(argument: Argument): Template = ArgumentTemplate(argument)
fun template(selector: Selector): Template = SelectorTemplate(selector)
fun template(application: Application): Template = ApplicationTemplate(application)

fun Template.apply(value: Value): Value =
	when (this) {
		is ArgumentTemplate -> argument.apply(value)
		is SelectorTemplate -> selector.apply(value)
		is ApplicationTemplate -> TODO()//application.apply(parameter)
	}
