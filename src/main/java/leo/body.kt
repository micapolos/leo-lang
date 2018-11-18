package leo

// TODO: What about recursion?
data class Body(
	val selectorTerm: Term<Selector>,
	val function: Function)

fun body(selectorTerm: Term<Selector>, function: Function) =
	Body(selectorTerm, function)

fun Body.apply(argument: Term<Nothing>): Term<Nothing>? =
	selectorTerm.apply(argument)?.let { selectedTerm ->
		function.invoke(selectedTerm)
	}

val Body.reflect: Field<Nothing>
	get() =
		bodyWord fieldTo term(
			selectorTerm.reflect(Selector::reflect),
			function.reflect)