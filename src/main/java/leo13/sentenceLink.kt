package leo13

import leo.base.Indent
import leo.base.append
import leo.base.appendableString
import leo.base.indent
import leo13.script.Script
import leo13.script.plus
import leo9.Stack
import leo9.push

data class SentenceLink(val sentence: Sentence, val line: SentenceLine) {
	override fun toString() = appendableString { it.append(this) }
}

fun link(sentence: Sentence, line: SentenceLine) = SentenceLink(sentence, line)
infix fun Sentence.linkTo(line: SentenceLine) = link(this, line)
infix fun SentenceLink.plus(line: SentenceLine) = sentence(this) linkTo line

fun Appendable.append(link: SentenceLink, indent: Indent = 0.indent): Appendable =
	append(link.sentence, indent).append("\n").append(indent).append(link.line, indent)

fun SentenceLink.lineSentenceOrNull(selectedWord: Word): Sentence? =
	line.sentenceOrNull(selectedWord) ?: sentence.lineSentenceOrNull(selectedWord)

fun SentenceLink.replaceOrNull(newLine: SentenceLine): SentenceLink? =
	line
		.replaceOrNull(newLine)
		?.let { sentence linkTo it }
		?: sentence.replaceOrNull(newLine)?.linkTo(line)

val SentenceLink.legacyScript: Script
	get() =
		sentence.legacyScript.plus(line.legacyLine)

fun Stack<SentenceScriptLine>.pushSentence(link: SentenceLink): Stack<SentenceScriptLine> =
	push(link.line.scriptLine).pushSentence(link.sentence)
