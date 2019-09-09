package leo13

sealed class Atom

object UnitAtom : Atom()
data class LinkAtom(val link: AtomLink) : Atom()
data class FunctionAtom(val function: AtomFunction) : Atom()
data class SentenceAtom(val sentence: Sentence) : Atom()

val atom: Atom = UnitAtom
fun atom(link: AtomLink): Atom = LinkAtom(link)
fun atom(function: AtomFunction): Atom = FunctionAtom(function)

val Atom.link: AtomLink get() = (this as LinkAtom).link
val Atom.sentence: Sentence get() = (this as SentenceAtom).sentence
val Atom.function: AtomFunction get() = (this as FunctionAtom).function

// === Atom to Sentence conversion

fun PatternScript.sentenceScript(atom: Atom): SentenceScript =
	patternOrNull?.sentence(atom)?.let { script(it) } ?: sentenceScript()

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
	if (link.leftAtom == atom) sentence(either.word lineTo either.script.sentenceScript(link.rightAtom))
	else choice.sentence(link.leftAtom.link.leftAtom linkTo link.rightAtom)

// === Sentence to Atom conversion

fun PatternScript.atom(sentenceScript: SentenceScript): Atom =
	if (patternOrNull == null) atom
	else patternOrNull.atom(sentenceScript.sentenceOrNull!!)

fun Pattern.atom(sentence: Sentence): Atom =
	when (this) {
		is WordPattern -> atom
		is LinePattern -> line.atom(sentence.lineOrNull!!)
		is LinkPattern -> atom(link.atom(sentence.linkOrNull!!))
		is ChoicePattern -> atom(choice.atomLink(sentence.scriptLineOrNull!!))
		is SentencePattern -> atom(sentence)
		is ArrowPattern -> TODO()
	}

fun PatternLine.atom(sentenceLine: SentenceLine): Atom =
	pattern.atom(sentenceLine.sentence)

fun PatternLink.atom(sentenceLink: SentenceLink): AtomLink =
	pattern.atom(sentenceLink.sentence) linkTo line.atom(sentenceLink.line)

fun Choice.atomLink(line: SentenceScriptLine): AtomLink =
	linkOrNull!!.atomLink(line)

fun ChoiceLink.atomLink(line: SentenceScriptLine): AtomLink =
	if (either.word == line.word) atom linkTo either.script.atom(line.script)
	else choice.atomLink(line).let { atomLink ->
		atom(atomLink.leftAtom linkTo atom) linkTo atomLink.rightAtom
	}
