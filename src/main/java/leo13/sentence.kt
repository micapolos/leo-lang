package leo13

import leo.base.*
import leo13.generic.List
import leo13.generic.foldOrNull
import leo13.generic.reverse
import leo13.script.Script
import leo9.Stack
import leo9.seq
import leo9.stack

val sentenceWriter = sentenceWriter<Sentence>(sentenceWord) { this }

sealed class Sentence {
	override fun toString() = appendableString { it.append(this) }
}

data class StartSentence(val start: SentenceStart) : Sentence() {
	override fun toString() = super.toString()
}

data class LinkSentence(val link: SentenceLink) : Sentence() {
	override fun toString() = super.toString()
}

fun sentence(start: SentenceStart): Sentence = StartSentence(start)
fun sentence(link: SentenceLink): Sentence = LinkSentence(link)
fun sentence(word: Word): Sentence = sentence(start(word))
fun sentence(line: SentenceLine): Sentence = sentence(start(line))

val sentenceReader = sentenceReader(sentenceWord) { read(this) }

val Sentence.startOrNull: SentenceStart? get() = (this as? StartSentence)?.start
val Sentence.linkOrNull: SentenceLink? get() = (this as? LinkSentence)?.link
val Sentence.wordOrNull: Word? get() = startOrNull?.wordOrNull
val Sentence.lineOrNull: SentenceLine? get() = startOrNull?.lineOrNull

val Sentence.optionLineOrNull: SentenceOptionLine?
	get() =
		when (this) {
			is StartSentence -> start.optionLineOrNull
			is LinkSentence -> null
		}

infix fun Sentence.plus(line: SentenceLine): Sentence =
	sentence(link(this, line))

infix fun Sentence.plus(word: Word): Sentence =
	sentence(word lineTo this)

fun sentence(line: SentenceLine, vararg lines: SentenceLine) =
	sentence(line).fold(lines) { plus(it) }

fun sentence(lineList: List<SentenceLine>) =
	lineList.reverse.run {
		sentence(head).foldOrNull(tail) { plus(it) }
	}

fun sentence(word: Word, vararg lines: SentenceLine) =
	sentence(word).fold(lines) { plus(it) }

fun Appendable.append(sentence: Sentence, indent: Indent = 0.indent): Appendable =
	when (sentence) {
		is StartSentence -> append(sentence.start)
		is LinkSentence -> append(sentence.link, indent)
	}

fun Sentence.lineSentenceOrNull(selectedWord: Word): Sentence? =
	when (this) {
		is StartSentence -> start.lineSentenceOrNull(selectedWord)
		is LinkSentence -> link.lineSentenceOrNull(selectedWord)
	}

fun Sentence.replaceOrNull(newLine: SentenceLine): Sentence? =
	when (this) {
		is StartSentence -> start.replaceOrNull(newLine)?.let { sentence(it) }
		is LinkSentence -> link.replaceOrNull(newLine)?.let { sentence(it) }
	}

fun Sentence.replaceOrNull(sentence: Sentence): Sentence? =
	when (sentence) {
		is StartSentence -> replaceOrNull(sentence.start)
		is LinkSentence -> replaceOrNull(sentence.link)
	}

fun Sentence.replaceOrNull(start: SentenceStart): Sentence? =
	when (start) {
		is WordSentenceStart -> null
		is LineSentenceStart -> replaceOrNull(start.line)
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

// Normalizing conversion from legacy Script
fun sentence(script: Script): Sentence =
	sentenceOption(script).sentenceOrNull!!

val Sentence.legacyScript
	get(): Script =
		when (this) {
			is StartSentence -> start.legacyScript
			is LinkSentence -> link.legacyScript
		}

fun Stack<SentenceOptionLine>.pushSentence(sentence: Sentence): Stack<SentenceOptionLine> =
	when (sentence) {
		is StartSentence -> pushSentence(sentence.start)
		is LinkSentence -> pushSentence(sentence.link)
	}

val Sentence.optionLineSeq: Seq<SentenceOptionLine>
	get() =
		stack<SentenceOptionLine>().pushSentence(this).seq
