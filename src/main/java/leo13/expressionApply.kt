package leo13

data class ExpressionApply(val expression: Expression) {
	override fun toString() = sentenceLine.toString()
}

fun ExpressionApply.atom(bindings: AtomBindings, functionAtom: Atom): Atom =
	expression.atom(bindings).let { parameterAtom ->
		functionAtom.function.apply(parameterAtom)
	}

val ExpressionApply.sentenceLine: SentenceLine
	get() =
		applyWord lineTo sentence(expression.sentenceLine)