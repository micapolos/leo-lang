package leo13

import leo.base.appendableString
import leo.base.fold
import leo.base.updateIfNotNull

data class Script(val sentenceOrNull: Sentence?) {
	override fun toString() = appendableString { it.append(this) }
}

fun script(sentenceOrNull: Sentence?) = Script(sentenceOrNull)

infix fun Script.plus(line: SentenceLine): Script =
	script(sentenceOrNull?.run { plus(line) } ?: sentence(line))

infix fun Script.plus(word: Word): Script =
	script(sentenceOrNull?.run { plus(word) } ?: sentence(word))

fun script(vararg lines: SentenceLine): Script =
	script(null).fold(lines) { plus(it) }

fun Appendable.append(script: Script) =
	updateIfNotNull(script.sentenceOrNull) { append(it) }