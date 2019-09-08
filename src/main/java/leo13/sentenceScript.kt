package leo13

import leo.base.appendableString
import leo.base.fold
import leo.base.updateIfNotNull

data class SentenceScript(val sentenceOrNull: Sentence?) {
	override fun toString() = appendableString { it.append(this) }
}

fun sentenceScript(sentenceOrNull: Sentence? = null) = SentenceScript(sentenceOrNull)
fun script(sentence: Sentence) = sentenceScript(sentence)

infix fun SentenceScript.plus(line: SentenceLine): SentenceScript =
	script(sentenceOrNull?.run { plus(line) } ?: sentence(line))

infix fun SentenceScript.plus(word: Word): SentenceScript =
	script(sentenceOrNull?.run { plus(word) } ?: sentence(word))

fun script(vararg lines: SentenceLine): SentenceScript =
	sentenceScript().fold(lines) { plus(it) }

fun Appendable.append(script: SentenceScript) =
	updateIfNotNull(script.sentenceOrNull) { append(it) }