package leo13

data class PatternLink(val pattern: Pattern, val line: PatternLine)

infix fun Pattern.linkTo(line: PatternLine) = PatternLink(this, line)

fun PatternLink.matches(link: SentenceLink): Boolean =
	pattern.matches(link.sentence) && line.matches(link.line)

val PatternLink.bodySentence: Sentence
	get() =
		pattern.bodySentence.plus(line.bodySentenceLine)

val SentenceLink.failableBodyPatternLink: Failable<PatternLink>
	get() =
		sentence.failableBodyPattern.failableMap {
			let { pattern ->
				line.failableBodyPatternLine.map {
					pattern linkTo this
				}
			}
		}

fun PatternLink.linePatternOrNull(word: Word): Pattern? =
	line.patternOrNull(word) ?: pattern.linePatternOrNull(word)

fun PatternLink.replaceOrNull(newLine: PatternLine): PatternLink? =
	line
		.replaceOrNull(newLine)
		?.let { pattern linkTo it }
		?: pattern.replaceOrNull(newLine)?.linkTo(line)

fun patternLink(link: SentenceLink): PatternLink =
	pattern(link.sentence) linkTo patternLine(link.line)