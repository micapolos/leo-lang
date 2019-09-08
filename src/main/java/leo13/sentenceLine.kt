package leo13

import leo.base.*

data class SentenceLine(val word: Word, val sentence: Sentence) {
	override fun toString() = appendableString { it.append(this) }
}

fun line(word: Word, sentence: Sentence) = SentenceLine(word, sentence)
infix fun String.plus(sentence: Sentence) = line(word(this), sentence)

fun Appendable.append(line: SentenceLine, indent: Indent = 0.indent): Appendable =
	append(line.word)
		.run {
			if (line.sentence is LinkSentence) append("\n").append(indent.inc).append(line.sentence, indent.inc)
			else append(": ").append(line.sentence, indent)
		}