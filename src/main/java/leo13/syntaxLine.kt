package leo13

data class SyntaxLine(val word: Word, val syntax: Syntax)

val SyntaxLine.sentenceLine
	get() =
		word lineTo syntax.sentence
