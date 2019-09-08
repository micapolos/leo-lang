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

fun sentence(string: String) =
	sentence(word(string))

infix fun Sentence.plus(line: SentenceLine): Sentence =
	sentence(link(this, line))

infix fun Sentence.plus(word: String): Sentence =
	sentence(word plus this)

fun sentence(line: SentenceLine, vararg lines: SentenceLine) =
	sentence(line).fold(lines) { plus(it) }

fun Appendable.append(sentence: Sentence, indent: Indent = 0.indent): Appendable =
	when (sentence) {
		is WordSentence -> append(sentence.word)
		is LineSentence -> append(sentence.line, indent)
		is LinkSentence -> append(sentence.link, indent)
	}
