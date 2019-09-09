package leo13

import leo.base.fold

data class AtomBindings(val linkOrNull: AtomBindingsLink?) {
	override fun toString() = sentenceLine.toString()
}

fun atomBindings(linkOrNull: AtomBindingsLink?) = AtomBindings(linkOrNull)
fun AtomBindings.plus(atom: Atom) = atomBindings(this linkTo atom)
fun atomBindings(vararg atoms: Atom) = atomBindings(null).fold(atoms) { plus(it) }

val AtomBindings.sentenceLine: SentenceLine
	get() =
		bindingsWord.lineTo(
			if (linkOrNull == null) sentence(noneWord)
			else linkOrNull.bodySentence)

val AtomBindings.bodySentenceScript: SentenceScript
	get() =
		if (linkOrNull == null) sentenceScript()
		else sentenceScript(linkOrNull.bodySentence)