package leo13

import leo.base.*
import leo13.script.Script
import leo9.Stack
import leo9.push
import leo9.seq
import leo9.stack

val sentenceWriter = sentenceWriter<Sentence>(sentenceWord) { this }

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

val sentenceReader = sentenceReader(sentenceWord) { read(this) }

val Sentence.wordOrNull: Word? get() = (this as? WordSentence)?.word
val Sentence.lineOrNull: SentenceLine? get() = (this as? LineSentence)?.line
val Sentence.linkOrNull: SentenceLink? get() = (this as? LinkSentence)?.link

val Sentence.optionLineOrNull: SentenceOptionLine?
	get() =
		when (this) {
			is WordSentence -> sentenceOptionLine(word)
			is LineSentence -> line.word lineTo option(line.sentence)
			is LinkSentence -> null
		}

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

fun Sentence.replaceOrNull(sentence: Sentence): Sentence? =
	when (sentence) {
		is WordSentence -> null
		is LineSentence -> replaceOrNull(sentence.line)
		is LinkSentence -> replaceOrNull(sentence.link)
	}

fun Sentence.replaceOrNull(link: SentenceLink): Sentence? =
	replaceOrNull(link.sentence)?.replaceOrNull(link.line)

fun Sentence.getOrNull(word: Word): Sentence? =
	lineOrNull
		?.sentence
		?.lineSentenceOrNull(word)
		?.let { sentence(word lineTo it) }

fun Sentence.setOrNull(newSentence: Sentence): Sentence? =
	lineOrNull?.run {
		sentence
			.replaceOrNull(newSentence)
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

val Sentence.failableOptionLine: Failable<SentenceOptionLine>
	get() =
		when (this) {
			is WordSentence -> success(sentenceOptionLine(word))
			is LineSentence -> success(line.optionLine)
			is LinkSentence -> failure(sentence(scriptWord))
		}

val Sentence.failableLink: Failable<SentenceLink>
	get() =
		(this as? LinkSentence)
			?.link
			?.run(::success)
			?: failure(sentence(linkWord))

// Normalizing conversion from legacy Script
fun sentence(script: Script): Sentence =
	sentenceOption(script).sentenceOrNull!!

val Sentence.legacyScript
	get(): Script =
		when (this) {
			is WordSentence -> leo13.script.script(word.toString())
			is LineSentence -> leo13.script.script(line.legacyLine)
			is LinkSentence -> link.legacyScript
		}

fun Stack<SentenceOptionLine>.pushSentence(sentence: Sentence): Stack<SentenceOptionLine> =
	when (sentence) {
		is WordSentence -> push(sentence.word lineTo sentenceOption())
		is LineSentence -> push(sentence.line.optionLine)
		is LinkSentence -> pushSentence(sentence.link)
	}

val Sentence.optionLineSeq: Seq<SentenceOptionLine>
	get() =
		stack<SentenceOptionLine>().pushSentence(this).seq
