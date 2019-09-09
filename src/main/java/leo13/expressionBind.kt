package leo13

data class ExpressionBind(val expression: Expression) {
	override fun toString() = sentenceLine.toString()
}

fun ExpressionBind.atom(bindings: AtomBindings, atom: Atom): Atom =
	expression.atom(bindings.plus(atom))

val ExpressionBind.sentenceLine: SentenceLine
	get() =
		bindWord lineTo sentence(expression.sentenceLine)