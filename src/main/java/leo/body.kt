package leo

// TODO: What about recursion?
data class Body(
	val selectorTermOrNull: Term<Selector>?,
	val function: Function)

fun body(selectorTermOrNull: Term<Selector>?, function: Function) =
	Body(selectorTermOrNull, function)

fun Body.apply(argument: Term<Nothing>): Term<Nothing>? =
	selectorTermOrNull?.apply(argument)?.let { selectedTerm ->
		function.invoke(selectedTerm)
	}

//val Body.reflect: Field<Nothing>
//	get() =
//		bodyWord fieldTo term(
//			selectorTerm.reflect(Selector::reflect),
//			function.reflect)