package leo13

data class ExpressionBinding(val previousBindingOrNull: ExpressionBinding?)

fun ExpressionBinding.atom(bindings: AtomBindings): Atom =
	atom(bindings.linkOrNull!!)

fun ExpressionBinding.atom(link: AtomBindingsLink): Atom =
	if (previousBindingOrNull == null) link.atom
	else previousBindingOrNull.atom(link.bindings)