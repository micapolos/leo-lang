package leo13

import leo.base.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.isEmpty
import leo13.script.lineSeq

data class SentenceOption(val sentenceOrNull: Sentence?) {
	override fun toString() = appendableString { it.append(this) }
}

fun sentenceOption(sentenceOrNull: Sentence? = null) = SentenceOption(sentenceOrNull)
fun option(sentence: Sentence) = sentenceOption(sentence)

infix fun SentenceOption.plus(line: SentenceLine): SentenceOption =
	option(plusSentence(line))

infix fun SentenceOption.plus(word: Word): SentenceOption =
	option(sentenceOrNull?.run { plus(word) } ?: sentence(word))

infix fun SentenceOption.plusSentence(line: SentenceLine): Sentence =
	sentenceOrNull?.run { plus(line) } ?: sentence(line)

fun option(line: SentenceLine, vararg lines: SentenceLine): SentenceOption =
	sentenceOption(sentence(line)).fold(lines) { plus(it) }

fun Appendable.append(option: SentenceOption) =
	updateIfNotNull(option.sentenceOrNull) { append(it) }

// Legacy conversion

fun sentenceOption(legacyScript: Script): SentenceOption =
	sentenceOption().fold(legacyScript.lineSeq) { plus(it) }

// Normalizing conversion from legacy Script
fun SentenceOption.plus(legacyLine: ScriptLine): SentenceOption =
	word(legacyLine.name).let { word ->
		if (legacyLine.rhs.isEmpty)
			if (sentenceOrNull == null) option(sentence(word))
			else option(sentence(word lineTo sentenceOrNull))
		else
			plus(word lineTo sentence(legacyLine.rhs))
	}

val SentenceOption.legacyScript
	get(): Script =
		if (sentenceOrNull == null) leo13.script.script()
		else sentenceOrNull.legacyScript

val SentenceOption.lineSeq: Seq<SentenceOptionLine>
	get() =
		sentenceOrNull?.optionLineSeq ?: seq()