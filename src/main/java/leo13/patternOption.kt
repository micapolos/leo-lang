package leo13

data class PatternOption(val patternOrNull: Pattern?)

fun patternOption(patternOrNull: Pattern? = null) = PatternOption(patternOrNull)
fun option(pattern: Pattern) = patternOption(pattern)

val PatternOption.bodySentenceOption: SentenceOption
	get() =
		if (patternOrNull == null) sentenceOption()
		else option(patternOrNull.bodySentence)
