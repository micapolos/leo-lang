package leo3

sealed class Template
data class ArgumentTemplate(val argument: Argument) : Template()
data class SelectorTemplate(val selector: Selector) : Template()
data class ApplicationTemplate(val application: Application) : Template()

fun Template.apply(term: Term): Term? =
	when (this) {
		is ArgumentTemplate -> term
		is SelectorTemplate -> selector.apply(term)
		is ApplicationTemplate -> application.apply(term)
	}
