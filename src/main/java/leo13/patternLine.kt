package leo13

data class PatternLine(val word: Word, val pattern: Pattern)

infix fun Word.lineTo(pattern: Pattern) = PatternLine(this, pattern)

fun PatternLine.matches(line: SentenceLine): Boolean =
	word == line.word && pattern.matches(line.sentence)

val PatternLine.bodySentenceLine: SentenceLine
	get() =
		word lineTo pattern.bodySentence

val PatternLine.bodySentence: Sentence
	get() =
		sentence(bodySentenceLine)

val SentenceLine.failableBodyPatternLine: Failable<PatternLine>
	get() =
		sentence.failableBodyPattern.map { word lineTo this }