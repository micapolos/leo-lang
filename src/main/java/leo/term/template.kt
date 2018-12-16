package leo.term

typealias Template = Term<Selector>

fun template(term: Term<Selector>): Template = term

fun Template.invoke(script: Script): Script? =
	when (this) {
		is ApplicationTerm -> receiver.termOrNull?.invoke(script)
			.apply(application.word, application.argument.termOrNull?.invoke(script))
		is ValueTerm -> script.select(value)
	}
