package leo13

data class LineCase(val word: Word, val syntax: Syntax)

infix fun Word.caseTo(syntax: Syntax) = LineCase(this, syntax)

val LineCase.sentenceLine: SentenceLine
	get() =
		caseWord lineTo sentence(word lineTo syntax.sentence)
