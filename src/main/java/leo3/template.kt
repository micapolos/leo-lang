package leo3

sealed class Template
data class ArgumentTemplate(val argument: Argument) : Template()
data class SelectorTemplate(val selector: Selector) : Template()
data class ApplicationTemplate(val application: Application) : Template()

fun template(argument: Argument): Template = ArgumentTemplate(argument)
fun template(selector: Selector): Template = SelectorTemplate(selector)
fun template(application: Application): Template = ApplicationTemplate(application)

fun Template.apply(parameter: Parameter): Result? =
	when (this) {
		is ArgumentTemplate -> argument.apply(parameter)
		is SelectorTemplate -> selector.apply(parameter)
		is ApplicationTemplate -> application.apply(parameter)
	}
