package leo13

import leo.base.Indent
import leo.base.append
import leo.base.appendableString
import leo.base.indent

data class SentenceLink(val sentence: Sentence, val line: SentenceLine) {
	override fun toString() = appendableString { it.append(this) }
}

fun link(sentence: Sentence, line: SentenceLine) = SentenceLink(sentence, line)
infix fun SentenceLink.plus(line: SentenceLine) = link(sentence(this), line)

fun Appendable.append(link: SentenceLink, indent: Indent = 0.indent): Appendable =
	append(link.sentence, indent).append("\n").append(indent).append(link.line, indent)