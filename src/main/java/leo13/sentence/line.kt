package leo13.sentence

import leo.base.*

data class Line(val word: Word, val sentence: Sentence) {
	override fun toString() = appendableString { it.append(this) }
}

fun line(word: Word, sentence: Sentence) = Line(word, sentence)
infix fun String.plus(sentence: Sentence) = line(word(this), sentence)

fun Appendable.append(line: Line, indent: Indent = 0.indent): Appendable =
	append(line.word)
		.run {
			if (line.sentence is LinkSentence) append("\n").append(indent.inc).append(line.sentence, indent.inc)
			else append(": ").append(line.sentence, indent)
		}