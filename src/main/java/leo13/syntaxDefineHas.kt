package leo13

import leo13.generic.List

data class SyntaxDefineHas(val wordList: List<Word>, val pattern: Pattern)

val SyntaxDefineHas.sentence: Sentence
	get() =
		sentence(sentenceStart(wordList)).plus(hasWord lineTo pattern.sentence)
