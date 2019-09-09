package leo13

data class ExpressionLink(val expression: Expression, val operation: ExpressionOperation)

infix fun Expression.linkTo(operation: ExpressionOperation) = ExpressionLink(this, operation)

fun ExpressionLink.atom(bindings: AtomBindings): Atom =
	operation.atom(bindings, expression.atom(bindings))
