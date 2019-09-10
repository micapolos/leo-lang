package leo13

data class PatternScript(val patternOrNull: Pattern?)

fun patternScript(patternOrNull: Pattern? = null) = PatternScript(patternOrNull)
fun script(pattern: Pattern) = patternScript(pattern)

val PatternScript.bodySentenceScript: SentenceOption
	get() =
		if (patternOrNull == null) sentenceOption()
		else option(patternOrNull.bodySentence)