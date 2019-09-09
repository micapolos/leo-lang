package leo13

data class ExpressionSwitch(val switchOrNull: ExpressionSwitch?, val operation: ExpressionOperation)

fun ExpressionSwitch.atom(bindings: AtomBindings, atom: Atom): Atom =
	TODO()