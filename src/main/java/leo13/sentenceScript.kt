package leo13

import leo.base.*
import leo13.script.Script
import leo13.script.ScriptLine
import leo13.script.isEmpty
import leo13.script.lineSeq

data class SentenceScript(val sentenceOrNull: Sentence?) {
	override fun toString() = appendableString { it.append(this) }
}

fun sentenceScript(sentenceOrNull: Sentence? = null) = SentenceScript(sentenceOrNull)
fun script(sentence: Sentence) = sentenceScript(sentence)

infix fun SentenceScript.plus(line: SentenceLine): SentenceScript =
	script(plusSentence(line))

infix fun SentenceScript.plus(word: Word): SentenceScript =
	script(sentenceOrNull?.run { plus(word) } ?: sentence(word))

infix fun SentenceScript.plusSentence(line: SentenceLine): Sentence =
	sentenceOrNull?.run { plus(line) } ?: sentence(line)

fun script(vararg lines: SentenceLine): SentenceScript =
	sentenceScript().fold(lines) { plus(it) }

fun Appendable.append(script: SentenceScript) =
	updateIfNotNull(script.sentenceOrNull) { append(it) }

// Legacy conversion

fun sentenceScript(legacyScript: Script): SentenceScript =
	sentenceScript().fold(legacyScript.lineSeq) { plus(it) }

// Normalizing conversion from legacy Script
fun SentenceScript.plus(legacyLine: ScriptLine): SentenceScript =
	word(legacyLine.name).let { word ->
		if (legacyLine.rhs.isEmpty)
			if (sentenceOrNull == null) script(sentence(word))
			else script(sentence(word lineTo sentenceOrNull))
		else
			plus(word lineTo sentence(legacyLine.rhs))
	}

val SentenceScript.legacyScript
	get(): Script =
		if (sentenceOrNull == null) leo13.script.script()
		else sentenceOrNull.legacyScript

val SentenceScript.lineSeq: Seq<SentenceScriptLine>
	get() =
		sentenceOrNull?.scriptLineSeq ?: seq()