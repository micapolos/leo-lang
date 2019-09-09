package leo13

data class ExpressionBind(val expression: Expression)

fun ExpressionBind.atom(bindings: AtomBindings, atom: Atom): Atom =
	expression.atom(bindings.plus(atom))