package leo13

data class PatternArrow(val firstPattern: Pattern, val secondPattern: Pattern)

infix fun Pattern.arrowTo(pattern: Pattern) = PatternArrow(this, pattern)

fun PatternArrow.matches(sentence: Sentence): Boolean =
	when (sentence) {
		is WordSentence -> false
		is LineSentence -> false
		is LinkSentence -> matches(sentence.link)
	}

fun PatternArrow.matches(link: SentenceLink): Boolean =
	TODO()