package leo13

data class AtomLink(val leftAtom: Atom, val rightAtom: Atom) {
	override fun toString() = atomLinkSentenceWriter.toString(this)
}

infix fun Atom.linkTo(rightAtom: Atom) = AtomLink(this, rightAtom)

val atomLinkSentenceWriter: SentenceLineWriter<AtomLink> = writer(
	linkWord,
	field(writer(leftWord, atomSentenceWriter)) { leftAtom },
	field(writer(rightWord, atomSentenceWriter)) { rightAtom })

val AtomLink.sentenceLine: SentenceLine
	get() =
		linkWord lineTo sentenceBody

val AtomLink.sentenceBody: Sentence
	get() =
		sentence(
			leftWord lineTo sentence(leftAtom.sentenceLine),
			rightWord lineTo sentence(rightAtom.sentenceLine))
