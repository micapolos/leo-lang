package leo13

data class AtomBindingsLink(val bindings: AtomBindings, val atom: Atom) {
	override fun toString() = sentenceLine.toString()
}

infix fun AtomBindings.linkTo(atom: Atom) = AtomBindingsLink(this, atom)

val AtomBindingsLink.sentenceLine: SentenceLine
	get() =
		linkWord lineTo bodySentence

val AtomBindingsLink.bodySentence: Sentence
	get() =
		bindings.bodySentenceScript.plusSentence(atom.sentenceLine)