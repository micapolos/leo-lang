package leo13

data class SyntaxDefineGives(val pattern: Pattern, val syntax: Syntax)

val SyntaxDefineGives.sentence: Sentence
	get() =
		pattern.sentence.plus(givenWord lineTo syntax.sentence)