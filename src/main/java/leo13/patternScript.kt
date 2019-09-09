package leo13

data class PatternScript(val patternOrNull: Pattern?)

fun patternScript(patternOrNull: Pattern? = null) = PatternScript(patternOrNull)
fun script(pattern: Pattern) = patternScript(pattern)

val PatternScript.bodySentenceScript: SentenceScript
	get() =
		if (patternOrNull == null) sentenceScript()
		else script(patternOrNull.bodySentence)