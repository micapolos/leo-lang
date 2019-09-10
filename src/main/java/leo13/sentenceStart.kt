package leo13

import leo.base.Indent
import leo.base.appendableString
import leo.base.fold
import leo.base.indent
import leo13.generic.List
import leo13.generic.foldOrNull
import leo13.script.Script
import leo9.Stack
import leo9.push

sealed class SentenceStart {
	override fun toString() = appendableString { it.append(this) }
}

data class WordSentenceStart(val word: Word) : SentenceStart() {
	override fun toString() = super.toString()
}

data class LineSentenceStart(val line: SentenceLine) : SentenceStart() {
	override fun toString() = super.toString()
}

fun start(word: Word): SentenceStart = WordSentenceStart(word)
fun start(line: SentenceLine): SentenceStart = LineSentenceStart(line)

fun start(word: Word, sentenceStartOrNull: SentenceStart?): SentenceStart =
	if (sentenceStartOrNull == null) start(word)
	else start(word lineTo sentence(sentenceStartOrNull))

fun start(word: Word, sentenceOrNull: Sentence?): SentenceStart =
	if (sentenceOrNull == null) start(word)
	else start(word lineTo sentenceOrNull)

fun SentenceStart.plus(word: Word): SentenceStart =
	start(word lineTo sentence(this))

val SentenceStart.wordOrNull get() = (this as? WordSentenceStart)?.word
val SentenceStart.lineOrNull get() = (this as? LineSentenceStart)?.line

fun Appendable.append(start: SentenceStart, indent: Indent = 0.indent): Appendable =
	when (start) {
		is WordSentenceStart -> append(start.word)
		is LineSentenceStart -> append(start.line, indent)
	}

fun SentenceStart.lineSentenceOrNull(selectedWord: Word): Sentence? =
	when (this) {
		is WordSentenceStart -> null
		is LineSentenceStart -> line.sentenceOrNull(selectedWord)
	}

fun SentenceStart.replaceOrNull(newLine: SentenceLine): SentenceStart? =
	when (this) {
		is WordSentenceStart -> null
		is LineSentenceStart -> line.replaceOrNull(newLine)?.let { start(it) }
	}

val SentenceStart.optionLineOrNull: SentenceOptionLine?
	get() =
		when (this) {
			is WordSentenceStart -> sentenceOptionLine(word)
			is LineSentenceStart -> line.word lineTo option(line.sentence)
		}

val SentenceStart.failableOptionLine: Failable<SentenceOptionLine>
	get() =
		when (this) {
			is WordSentenceStart -> success(sentenceOptionLine(word))
			is LineSentenceStart -> success(line.optionLine)
		}

val SentenceStart.legacyScript
	get(): Script =
		when (this) {
			is WordSentenceStart -> leo13.script.script(word.toString())
			is LineSentenceStart -> leo13.script.script(line.legacyLine)
		}

fun Stack<SentenceOptionLine>.pushSentence(start: SentenceStart): Stack<SentenceOptionLine> =
	when (start) {
		is WordSentenceStart -> push(start.word lineTo sentenceOption())
		is LineSentenceStart -> push(start.line.optionLine)
	}

fun sentenceStart(wordList: List<Word>): SentenceStart =
	wordList.run {
		start(head).foldOrNull(tail) { plus(it) }
	}

fun sentenceStart(word: Word, vararg words: Word) =
	start(word).fold(words) { plus(it) }