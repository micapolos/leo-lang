package leo13

data class PatternLink(val pattern: Pattern, val choice: LineChoice) {
	override fun toString() = patternLinkSentenceWriter.toString(this)
}

infix fun Pattern.linkTo(choice: LineChoice) = PatternLink(this, choice)

infix fun Pattern.linkTo(line: PatternLine) = linkTo(choice(either(line)))

val patternLinkSentenceWriter: SentenceLineWriter<PatternLink> =
	writer(
		linkWord,
		field(patternSentenceWriter) { pattern },
		field(lineChoiceSentenceWriter) { choice })

fun PatternLink.matches(link: SentenceLink): Boolean =
	pattern.matches(link.sentence) && choice.matches(link.line)

fun patternLink(link: SentenceLink): PatternLink =
	pattern(link.sentence) linkTo patternLine(link.line)

fun PatternLink.contains(link: PatternLink): Boolean =
	this == link