package leo13

data class AtomFunction(val bindings: AtomBindings, val expression: Expression)

fun function(bindings: AtomBindings, expression: Expression) = AtomFunction(bindings, expression)

fun AtomFunction.apply(atom: Atom) =
	expression.atom(bindings.plus(atom))
