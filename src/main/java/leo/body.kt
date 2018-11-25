package leo

import leo.base.string

// TODO: What about recursion?
data class Body(
	val selectorTerm: Term<Selector>,
	val function: Function) {
	override fun toString() = reflect.string
}

fun body(selectorTerm: Term<Selector>, function: Function) =
	Body(selectorTerm, function)

fun Body.apply(argument: Term<Nothing>): Term<Nothing> =
	selectorTerm.apply(argument).let { selectedTerm ->
		function.invoke(selectedTerm)
	}

val Body.reflect: Field<Nothing>
	get() =
		bodyWord fieldTo term(
			selectorWord fieldTo selectorTerm.reflectMeta(Selector::reflect),
			functionReflect)