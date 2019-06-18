package lambda

class Function(val fn: (Term) -> Term) {
	override fun toString() = code
	override fun equals(other: Any?) = other is Function && eq(other)
}

fun function(fn: (Term) -> Term) = Function(fn)

operator fun Function.invoke(term: Term) = fn(term)

fun Function.eq(function: Function) = term(newVariable).let { variable -> fn(variable).eq(function.fn(variable)) }

val Function.code get() = newVariable.let { variable -> "${variable.code} -> ${fn(term(variable)).code}" }
