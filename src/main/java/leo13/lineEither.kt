package leo13

data class LineEither(val line: PatternLine) {
	override fun toString() = lineEitherSentenceWriter.toString(this)
}

fun either(line: PatternLine) = LineEither(line)

val lineEitherSentenceWriter =
	sentenceWriter<LineEither>(eitherWord) {
		patternLineSentenceWriter.bodySentence(line)
	}

fun LineEither.matches(line: SentenceLine): Boolean =
	this.line.matches(line)

fun LineEither.contains(either: LineEither): Boolean =
	line.contains(either.line)
