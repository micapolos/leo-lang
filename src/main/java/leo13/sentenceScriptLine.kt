package leo13

data class SentenceScriptLine(val word: Word, val script: SentenceScript)

infix fun Word.lineTo(script: SentenceScript) = SentenceScriptLine(this, script)

val SentenceScriptLine.sentence: Sentence
	get() =
		if (script.sentenceOrNull == null) sentence(word)
		else sentence(word lineTo script.sentenceOrNull)

fun sentence(scriptLine: SentenceScriptLine): Sentence =
	scriptLine.sentence

infix fun Word.sentenceLineTo(line: SentenceScriptLine): SentenceLine =
	this lineTo line.sentence

fun sentenceScriptLine(word: Word) =
	word lineTo sentenceScript()