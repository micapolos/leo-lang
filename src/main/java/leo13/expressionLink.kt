package leo13

data class ExpressionLink(val expression: Expression, val operation: ExpressionOperation) {
	override fun toString() = sentenceLine.toString()
}

infix fun Expression.linkTo(operation: ExpressionOperation) = ExpressionLink(this, operation)

fun ExpressionLink.atom(bindings: AtomBindings): Atom =
	operation.atom(bindings, expression.atom(bindings))

val ExpressionLink.sentenceLine: SentenceLine
	get() =
		linkWord lineTo bodySentence

val ExpressionLink.bodySentence: Sentence
	get() =
		sentence(expression.sentenceLine, operation.sentenceLine)