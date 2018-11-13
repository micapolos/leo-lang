package leo

// TODO: What about recursion?
data class Body(
	val selectorTerm: Term<Selector>,
	val function: Function)

fun body(selectorTerm: Term<Selector>, function: Function) =
	Body(selectorTerm, function)

fun Body.apply(argument: Term<Value>): Term<Value> =
	function.invoke(selectorTerm.apply(argument))

val Body.reflect: Field<Value>
	get() =
		bodyWord fieldTo term(
			selectorTerm.reflect { selector -> term(selector.reflect) },
			function.reflect)