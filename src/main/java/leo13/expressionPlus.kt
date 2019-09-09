package leo13

data class ExpressionPlus(val expression: Expression)

fun plus(expression: Expression) = ExpressionPlus(expression)

fun ExpressionPlus.atom(bindings: AtomBindings, atom: Atom): Atom =
	atom(atom linkTo expression.atom(bindings))
