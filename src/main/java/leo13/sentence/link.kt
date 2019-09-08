package leo13.sentence

import leo.base.Indent
import leo.base.append
import leo.base.appendableString
import leo.base.indent
import leo13.Sentence
import leo13.append
import leo13.sentence

data class Link(val sentence: Sentence, val line: Line) {
	override fun toString() = appendableString { it.append(this) }
}

fun link(sentence: Sentence, line: Line) = Link(sentence, line)
infix fun Link.plus(line: Line) = link(sentence(this), line)

fun Appendable.append(link: Link, indent: Indent = 0.indent): Appendable =
	append(link.sentence, indent).append("\n").append(indent).append(link.line, indent)