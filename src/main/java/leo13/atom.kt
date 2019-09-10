package leo13

sealed class Atom {
	override fun toString() = atomSentenceWriter.toString(this)
}

data class EmptyAtom(val empty: Empty) : Atom()

data class LinkAtom(val link: AtomLink) : Atom() {
	override fun toString() = super.toString()
}

data class FunctionAtom(val function: AtomFunction) : Atom() {
	override fun toString() = super.toString()
}

data class SentenceAtom(val sentence: Sentence) : Atom() {
	override fun toString() = super.toString()
}

val atomSentenceWriter =
	recursiveWriter<Atom> {
		sealedSentenceWriter(
			atomWord,
			emptySentenceWriter,
			atomLinkSentenceWriter,
			atomFunctionSentenceWriter,
			sentenceWriter
		) { fn1, fn2, fn3, fn4 ->
			when (this) {
				is EmptyAtom -> empty.fn1()
				is LinkAtom -> link.fn2()
				is FunctionAtom -> function.fn3()
				is SentenceAtom -> sentence.fn4()
			}
		}
	}

val emptyAtom: Atom = EmptyAtom(empty)
fun atom(link: AtomLink): Atom = LinkAtom(link)
fun atom(function: AtomFunction): Atom = FunctionAtom(function)
fun atom(sentence: Sentence): Atom = SentenceAtom(sentence)

val Atom.link: AtomLink get() = (this as LinkAtom).link
val Atom.sentence: Sentence get() = (this as SentenceAtom).sentence
val Atom.function: AtomFunction get() = (this as FunctionAtom).function

val Atom.sentenceLine: SentenceLine
	get() =
		atomWord lineTo bodySentence

val Atom.bodySentence: Sentence
	get() =
		when (this) {
			is EmptyAtom -> sentence(unitWord)
			is LinkAtom -> sentence(link.sentenceLine)
			is FunctionAtom -> sentence(function.sentenceLine)
			is SentenceAtom -> sentence(sentenceWord lineTo sentence)
		}

// === Atom to Sentence conversion

fun PatternOption.sentenceOption(atom: Atom): SentenceOption =
	patternOrNull?.sentence(atom)?.let { option(it) } ?: sentenceOption()

fun Pattern.sentence(atom: Atom): Sentence =
	when (this) {
		is WordPattern -> sentence(word)
		is LinePattern -> sentence(line.sentenceLine(atom))
		is LinkPattern -> sentence(link.sentenceLink(atom.link))
		is ChoicePattern -> choice.sentence(atom.link)
		is SentencePattern -> atom.sentence
		is ArrowPattern -> error("ArrowPattern")
	}

fun PatternLine.sentenceLine(atom: Atom): SentenceLine =
	word lineTo pattern.sentence(atom)

fun PatternLink.sentenceLink(link: AtomLink): SentenceLink =
	pattern.sentence(link.leftAtom) linkTo line.sentenceLine(link.rightAtom)

fun Choice.sentence(link: AtomLink): Sentence =
	linkOrNull!!.sentence(link)

fun ChoiceLink.sentence(link: AtomLink): Sentence =
	if (link.leftAtom == emptyAtom) sentence(either.word lineTo either.option.sentenceOption(link.rightAtom))
	else choice.sentence(link.leftAtom.link.leftAtom linkTo link.rightAtom)

// === Sentence to Atom conversion

fun PatternOption.atom(sentenceScript: SentenceOption): Atom =
	if (patternOrNull == null) emptyAtom
	else patternOrNull.atom(sentenceScript.sentenceOrNull!!)

fun Pattern.atom(sentence: Sentence): Atom =
	when (this) {
		is WordPattern -> emptyAtom
		is LinePattern -> line.atom(sentence.lineOrNull!!)
		is LinkPattern -> atom(link.atom(sentence.linkOrNull!!))
		is ChoicePattern -> atom(choice.atomLink(sentence.optionLineOrNull!!))
		is SentencePattern -> atom(sentence)
		is ArrowPattern -> throw IllegalArgumentException()
	}

fun PatternLine.atom(sentenceLine: SentenceLine): Atom =
	pattern.atom(sentenceLine.sentence)

fun PatternLink.atom(sentenceLink: SentenceLink): AtomLink =
	pattern.atom(sentenceLink.sentence) linkTo line.atom(sentenceLink.line)

fun Choice.atomLink(line: SentenceOptionLine): AtomLink =
	linkOrNull!!.atomLink(line)

fun ChoiceLink.atomLink(line: SentenceOptionLine): AtomLink =
	if (either.word == line.word) emptyAtom linkTo either.option.atom(line.option)
	else choice.atomLink(line).let { atomLink ->
		atom(atomLink.leftAtom linkTo emptyAtom) linkTo atomLink.rightAtom
	}
