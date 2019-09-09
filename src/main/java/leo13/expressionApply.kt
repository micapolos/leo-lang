package leo13

data class ExpressionApply(val expression: Expression)

fun ExpressionApply.atom(bindings: AtomBindings, atom: Atom): Atom =
	TODO()