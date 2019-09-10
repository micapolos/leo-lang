package leo13

import leo.base.*
import leo13.script.ScriptLine
import leo13.script.lineTo

data class SentenceLine(val word: Word, val sentence: Sentence) {
	override fun toString() = appendableString { it.append(this) }
}

fun line(word: Word, sentence: Sentence) = SentenceLine(word, sentence)

infix fun String.plus(sentence: Sentence) = line(word(this), sentence)
infix fun Word.lineTo(sentence: Sentence) = line(this, sentence)

fun Appendable.append(line: SentenceLine, indent: Indent = 0.indent): Appendable =
	append(line.word)
		.run {
			if (line.sentence is LinkSentence) append("\n").append(indent.inc).append(line.sentence, indent.inc)
			else append(": ").append(line.sentence, indent)
		}

fun SentenceLine.failableSentence(word: Word): Failable<Sentence> =
	this.word.failableUnit(word).map { sentence }

fun SentenceLine.sentenceOrNull(selectedWord: Word): Sentence? =
	notNullIf(word == selectedWord) { sentence }

fun SentenceLine.replaceOrNull(newLine: SentenceLine): SentenceLine? =
	notNullIf(word == newLine.word) { newLine }

val SentenceLine.legacyLine: ScriptLine
	get() =
		word.toString() lineTo sentence.legacyScript

val SentenceLine.optionLine: SentenceOptionLine
	get() =
		word lineTo option(sentence)
