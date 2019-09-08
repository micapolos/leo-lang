package leo13

data class ValueLine(val word: Word, val value: Value)

infix fun Word.lineTo(value: Value) = ValueLine(this, value)

val ValueLine.sentenceLine: SentenceLine
	get() =
		word lineTo value.sentence

val ValueLine.patternLine: PatternLine
	get() =
		word lineTo value.pattern
