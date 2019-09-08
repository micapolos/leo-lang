package leo13

data class Either(val word: Word, val pattern: Pattern)

infix fun Word.eitherTo(pattern: Pattern) = Either(this, pattern)

fun Either.matches(line: SentenceLine): Boolean =
	word == line.word && pattern.matches(line.sentence)

val Either.sentenceLine: SentenceLine
	get() =
		eitherWord lineTo bodySentence

val Either.bodySentenceLine: SentenceLine
	get() =
		word lineTo pattern.bodySentence

val Either.bodySentence: Sentence
	get() =
		sentence(bodySentenceLine)

val SentenceLine.failableBodyEither: Failable<Either>
	get() =
		sentence.failableBodyPattern.map { word eitherTo this }

val Sentence.failableBodyEither: Failable<Either>
	get() =
		failableLine.failableMap { failableBodyEither }

val SentenceLine.failableEither: Failable<Either>
	get() =
		failableSentence(eitherWord).failableMap { failableBodyEither }
