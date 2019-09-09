package leo13

data class AtomLink(val leftAtom: Atom, val rightAtom: Atom) {
	override fun toString() = sentenceLine.toString()
}

infix fun Atom.linkTo(rightAtom: Atom) = AtomLink(this, rightAtom)

val AtomLink.sentenceLine: SentenceLine
	get() =
		linkWord lineTo sentenceBody

val AtomLink.sentenceBody: Sentence
	get() =
		sentence(
			leftWord lineTo sentence(leftAtom.sentenceLine),
			rightWord lineTo sentence(rightAtom.sentenceLine))
