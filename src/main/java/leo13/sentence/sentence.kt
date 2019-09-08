package leo13.sentence

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

data class LineSentence(val line: Line) : Sentence() {
	override fun toString() = super.toString()
}

data class LinkSentence(val link: Link) : Sentence() {
	override fun toString() = super.toString()
}

fun sentence(word: Word): Sentence = WordSentence(word)
fun sentence(line: Line): Sentence = LineSentence(line)
fun sentence(link: Link): Sentence = LinkSentence(link)

fun sentence(string: String) = sentence(word(string))
infix fun Sentence.plus(line: Line): Sentence = sentence(link(this, line))
infix fun Sentence.plus(word: String): Sentence = sentence(word + this)
fun sentence(line: Line, vararg lines: Line) = sentence(line).fold(lines) { plus(it) }

fun Appendable.append(sentence: Sentence, indent: Indent = 0.indent): Appendable =
	when (sentence) {
		is WordSentence -> append(sentence.word)
		is LineSentence -> append(sentence.line, indent)
		is LinkSentence -> append(sentence.link, indent)
	}