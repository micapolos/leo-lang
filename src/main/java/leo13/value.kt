package leo13

data class Value(val sentence: Sentence, val pattern: Pattern) {
	override fun toString() = sentenceLine.toString()
}

fun value(sentence: Sentence, pattern: Pattern) = Value(sentence, pattern)

fun value(sentence: Sentence): Value =
	value(sentence, pattern(sentence))

infix fun Sentence.valueOf(pattern: Pattern) = Value(this, pattern)

fun value(line: ValueLine): Value =
	sentence(line.sentenceLine).valueOf(pattern(line.patternLine))

infix fun Value.plus(line: ValueLine): Value =
	sentence
		.plus(line.sentenceLine)
		.valueOf(pattern.plus(line.patternLine))

infix fun Value?.orNullPlus(line: ValueLine): Value =
	this?.plus(line) ?: value(line)

val Value.sentenceLine: SentenceLine
	get() =
		valueWord lineTo sentence(sentenceWord lineTo sentence, pattern.sentenceLine)