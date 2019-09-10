package leo13

data class SentenceOptionLine(val word: Word, val option: SentenceOption)

infix fun Word.lineTo(option: SentenceOption) = SentenceOptionLine(this, option)

val SentenceOptionLine.sentence: Sentence
	get() =
		if (option.sentenceOrNull == null) sentence(word)
		else sentence(word lineTo option.sentenceOrNull)

fun sentence(optionLine: SentenceOptionLine): Sentence =
	optionLine.sentence

infix fun Word.sentenceLineTo(optionLine: SentenceOptionLine): SentenceLine =
	this lineTo optionLine.sentence

fun sentenceOptionLine(word: Word): SentenceOptionLine =
	word lineTo sentenceOption()