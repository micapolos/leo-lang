package leo13

data class AtomFunction(val bindings: AtomBindings, val expression: Expression) {
	override fun toString() = atomFunctionSentenceWriter.toString(this)
}

val atomFunctionSentenceWriter: SentenceWriter<AtomFunction> = writer(
	functionWord,
	field(TODO() as SentenceWriter<AtomBindings>) { bindings },
	field(TODO() as SentenceWriter<Expression>) { expression })

fun function(bindings: AtomBindings, expression: Expression) = AtomFunction(bindings, expression)

fun AtomFunction.apply(atom: Atom) =
	expression.atom(bindings.plus(atom))

val AtomFunction.sentenceLine: SentenceLine
	get() =
		functionWord lineTo sentence(bindings.sentenceLine, expression.sentenceLine)