package leo13

data class ExpressionLink(val expression: Expression, val operation: ExpressionOperation)

fun ExpressionLink.atom(bindings: AtomBindings): Atom =
	operation.atom(bindings, expression.atom(bindings))
