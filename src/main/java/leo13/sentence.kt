package leo13

import leo.base.Indent
import leo.base.appendableString
import leo.base.fold
import leo.base.indent

sealed class Sentence {
	override fun toString() = appendableString { it.append(this) }
}

data class WordSentence(val word: Word) : Sentence() {
	override fun toString() = super.toString()
}

data class LineSentence(val line: SentenceLine) : Sentence() {
	override fun toString() = super.toString()
}

data class LinkSentence(val link: SentenceLink) : Sentence() {
	override fun toString() = super.toString()
}

fun sentence(word: Word): Sentence = WordSentence(word)
fun sentence(line: SentenceLine): Sentence = LineSentence(line)
fun sentence(link: SentenceLink): Sentence = LinkSentence(link)

val Sentence.lineOrNull: SentenceLine? get() = (this as? LineSentence)?.line

infix fun Sentence.plus(line: SentenceLine): Sentence =
	sentence(link(this, line))

infix fun Sentence.plus(word: Word): Sentence =
	sentence(word lineTo this)

fun sentence(line: SentenceLine, vararg lines: SentenceLine) =
	sentence(line).fold(lines) { plus(it) }

fun sentence(word: Word, vararg lines: SentenceLine) =
	sentence(word).fold(lines) { plus(it) }

fun Appendable.append(sentence: Sentence, indent: Indent = 0.indent): Appendable =
	when (sentence) {
		is WordSentence -> append(sentence.word)
		is LineSentence -> append(sentence.line, indent)
		is LinkSentence -> append(sentence.link, indent)
	}

fun Sentence.lineSentenceOrNull(selectedWord: Word): Sentence? =
	when (this) {
		is WordSentence -> null
		is LineSentence -> line.sentenceOrNull(selectedWord)
		is LinkSentence -> link.lineSentenceOrNull(selectedWord)
	}

fun Sentence.replaceOrNull(newLine: SentenceLine): Sentence? =
	when (this) {
		is WordSentence -> null
		is LineSentence -> line.replaceOrNull(newLine)?.let { sentence(it) }
		is LinkSentence -> link.replaceOrNull(newLine)?.let { sentence(it) }
	}

fun Sentence.getOrNull(word: Word): Sentence? =
	lineOrNull
		?.sentence
		?.lineSentenceOrNull(word)
		?.let { sentence(word lineTo it) }

fun Sentence.setOrNull(line: SentenceLine): Sentence? =
	lineOrNull?.run {
		sentence
			.replaceOrNull(line)
			?.let { sentence(word lineTo it) }
	}

val Sentence.failableWord: Failable<Word>
	get() =
		(this as? WordSentence)
			?.word
			?.run(::success)
			?: failure(sentence(wordWord))

val Sentence.failableLine: Failable<SentenceLine>
	get() =
		(this as? LineSentence)
			?.line
			?.run(::success)
			?: failure(sentence(lineWord))

val Sentence.failableLink: Failable<SentenceLink>
	get() =
		(this as? LinkSentence)
			?.link
			?.run(::success)
			?: failure(sentence(linkWord))
