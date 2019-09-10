package leo13

data class PatternArrow(val firstPattern: Pattern, val secondPattern: Pattern)

infix fun Pattern.arrowTo(pattern: Pattern) = PatternArrow(this, pattern)

val patternArrowSentenceWriter: SentenceLineWriter<PatternArrow> =
	writer(
		arrowWord,
		field(writer(leftWord, patternSentenceWriter)) { firstPattern },
		field(writer(rightWord, patternSentenceWriter)) { secondPattern })

fun PatternArrow.contains(arrow: PatternArrow): Boolean =
	this == arrow
