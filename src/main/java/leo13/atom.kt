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
		sealedWriter(
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
		is StartPattern -> start.sentence(atom)
		is LinkPattern -> sentence(link.sentenceLink(atom.link))
	}

fun PatternStart.sentence(atom: Atom): Sentence =
	when (this) {
		is WordChoicePatternStart -> sentence(choice.word(atom))
		is LineChoicePatternStart -> sentence(choice.sentenceLine(atom.link))
		is AnyPatternStart -> atom.sentence
		is ArrowPatternStart -> error("ArrowPattern")
	}

fun PatternLine.sentenceLine(atom: Atom): SentenceLine =
	word lineTo pattern.sentence(atom)

fun PatternLink.sentenceLink(link: AtomLink): SentenceLink =
	pattern.sentence(link.leftAtom) linkTo choice.sentenceLine(link.rightAtom.link)

fun WordChoice.word(atom: Atom): Word =
	if (atom == emptyAtom) eitherList.head.word
	else wordChoice(eitherList.tail!!).word(atom(atom.link.leftAtom linkTo atom))

fun LineChoice.sentenceLine(link: AtomLink): SentenceLine =
	if (link.leftAtom == emptyAtom) eitherList.head.line.word lineTo eitherList.head.line.pattern.sentence(link.rightAtom)
	else lineChoice(eitherList.tail!!).sentenceLine(link.leftAtom.link.leftAtom linkTo link.rightAtom)

// === Sentence to Atom conversion

fun PatternOption.atom(sentenceScript: SentenceOption): Atom =
	if (patternOrNull == null) emptyAtom
	else patternOrNull.atom(sentenceScript.sentenceOrNull!!)

fun Pattern.atom(sentence: Sentence): Atom =
	when (this) {
		is StartPattern -> start.atom(sentence)
		is LinkPattern -> atom(link.atom(sentence.linkOrNull!!))
	}

fun PatternStart.atom(sentence: Sentence): Atom =
	when (this) {
		is WordChoicePatternStart -> emptyAtom
		is LineChoicePatternStart -> emptyAtom
		is AnyPatternStart -> atom(sentence)
		is ArrowPatternStart -> throw IllegalArgumentException()
	}

fun PatternLine.atom(sentenceLine: SentenceLine): Atom =
	pattern.atom(sentenceLine.sentence)

fun PatternLink.atom(link: SentenceLink): AtomLink =
	pattern.atom(link.sentence) linkTo atom(choice.atomLink(link.line))

fun WordChoice.atom(word: Word): Atom =
	if (eitherList.head.word == word) emptyAtom
	else wordChoice(eitherList.tail!!).atom(word).let { atom ->
		atom(atom linkTo emptyAtom)
	}

fun LineChoice.atomLink(line: SentenceLine): AtomLink =
	if (eitherList.head.line.word == line.word)
		emptyAtom linkTo eitherList.head.line.pattern.atom(line.sentence)
	else
		lineChoice(eitherList.tail!!).atomLink(line).let { atomLink ->
			atom(atomLink.leftAtom linkTo emptyAtom) linkTo atomLink.rightAtom
		}
