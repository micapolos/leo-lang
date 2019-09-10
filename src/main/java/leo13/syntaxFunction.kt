package leo13

data class SyntaxFunction(val pattern: Pattern, val syntax: Syntax)

val SyntaxFunction.sentenceLine: SentenceLine
	get() =
		functionWord lineTo sentence(
			patternSentenceWriter
				.bodySentence(pattern)
				.linkTo(givesWord lineTo syntax.sentence))