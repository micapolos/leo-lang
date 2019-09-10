package leo13

data class SyntaxCase(val word: Word, val syntax: Syntax)

infix fun Word.caseTo(syntax: Syntax) = SyntaxCase(this, syntax)

val SyntaxCase.sentenceLine: SentenceLine
	get() =
		caseWord lineTo sentence(word lineTo syntax.sentence)
