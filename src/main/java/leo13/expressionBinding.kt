package leo13

data class ExpressionBinding(val previousBindingOrNull: ExpressionBinding?) {
	override fun toString() = sentenceLine.toString()
}

fun expressionBinding(previousBindingOrNull: ExpressionBinding?) = ExpressionBinding(previousBindingOrNull)
val lastExpressionBinding = ExpressionBinding(null)
val ExpressionBinding.previous: ExpressionBinding get() = ExpressionBinding(this)

fun ExpressionBinding.atom(bindings: AtomBindings): Atom =
	atom(bindings.linkOrNull!!)

fun ExpressionBinding.atom(link: AtomBindingsLink): Atom =
	if (previousBindingOrNull == null) link.atom
	else previousBindingOrNull.atom(link.bindings)

val ExpressionBinding.sentenceLine: SentenceLine
	get() =
		bindingWord lineTo sentence(zeroWord) // TODO()