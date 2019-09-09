package leo13

sealed class Expression {
	override fun toString() = sentenceLine.toString()
}

data class AtomExpression(val atom: Atom) : Expression() {
	override fun toString() = super.toString()
}

data class LinkExpression(val link: ExpressionLink) : Expression() {
	override fun toString() = super.toString()
}

data class BindingExpression(val binding: ExpressionBinding) : Expression() {
	override fun toString() = super.toString()
}

fun expression(atom: Atom): Expression = AtomExpression(atom)
fun expression(link: ExpressionLink): Expression = LinkExpression(link)
fun expression(binding: ExpressionBinding): Expression = BindingExpression(binding)

fun Expression.atom(bindings: AtomBindings): Atom =
	when (this) {
		is AtomExpression -> atom
		is LinkExpression -> link.atom(bindings)
		is BindingExpression -> binding.atom(bindings)
	}

val Expression.sentenceLine: SentenceLine
	get() =
		expectedWord lineTo bodySentence

val Expression.bodySentenceLine: SentenceLine
	get() =
		when (this) {
			is AtomExpression -> atom.sentenceLine
			is LinkExpression -> link.sentenceLine
			is BindingExpression -> binding.sentenceLine
		}

val Expression.bodySentence: Sentence
	get() =
		sentence(bodySentenceLine)