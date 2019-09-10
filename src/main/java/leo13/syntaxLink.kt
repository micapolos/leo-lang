package leo13

data class SyntaxLink(val syntax: Syntax, val operation: SyntaxOperation)

val SyntaxLink.sentenceLink: SentenceLink
	get() =
		syntax.sentence linkTo operation.sentenceLine