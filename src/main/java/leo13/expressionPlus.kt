package leo13

data class ExpressionPlus(val expression: Expression) {
	override fun toString() = sentenceLine.toString()
}

fun plus(expression: Expression) = ExpressionPlus(expression)

fun ExpressionPlus.atom(bindings: AtomBindings, atom: Atom): Atom =
	atom(atom linkTo expression.atom(bindings))

val ExpressionPlus.sentenceLine: SentenceLine
	get() =
		plusWord lineTo sentence(expression.sentenceLine)