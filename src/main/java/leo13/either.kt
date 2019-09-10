package leo13

data class Either(val word: Word, val option: PatternOption)

infix fun Word.eitherTo(option: PatternOption) = Either(this, option)
fun either(word: Word) = word eitherTo patternOption()

fun Either.matches(otherWord: Word): Boolean =
	word == otherWord && option.patternOrNull == null

fun Either.matches(line: SentenceLine): Boolean =
	word == line.word && option.patternOrNull != null && option.patternOrNull.matches(line.sentence)

val Either.sentenceLine: SentenceLine
	get() =
		eitherWord lineTo bodySentenceOptionLine.sentence

val Either.bodySentenceOptionLine: SentenceOptionLine
	get() =
		word lineTo option.bodySentenceOption

val SentenceOptionLine.failableBodyEither: Failable<Either>
	get() =
		if (option.sentenceOrNull == null) success(either(word))
		else option.sentenceOrNull.failableBodyPattern.map { word eitherTo option(this) }

val Sentence.failableBodyEither: Failable<Either>
	get() =
		failableOptionLine.failableMap { failableBodyEither }

val SentenceLine.failableEither: Failable<Either>
	get() =
		failableSentence(eitherWord).failableMap { failableBodyEither }

fun Either.contains(otherWord: Word): Boolean =
	word == otherWord && option.patternOrNull == null

fun Either.contains(line: PatternLine): Boolean =
	word == line.word && option.patternOrNull != null && option.patternOrNull == line.pattern