package leo13

data class Either(val word: Word, val script: PatternScript)

infix fun Word.eitherTo(script: PatternScript) = Either(this, script)
fun either(word: Word) = word eitherTo patternScript()

fun Either.matches(otherWord: Word): Boolean =
	word == otherWord && script.patternOrNull == null

fun Either.matches(line: SentenceLine): Boolean =
	word == line.word && script.patternOrNull != null && script.patternOrNull.matches(line.sentence)

val Either.sentenceLine: SentenceLine
	get() =
		eitherWord lineTo bodySentenceOptionLine.sentence

val Either.bodySentenceOptionLine: SentenceOptionLine
	get() =
		word lineTo script.bodySentenceScript

val SentenceOptionLine.failableBodyEither: Failable<Either>
	get() =
		if (option.sentenceOrNull == null) success(either(word))
		else option.sentenceOrNull.failableBodyPattern.map { word eitherTo script(this) }

val Sentence.failableBodyEither: Failable<Either>
	get() =
		failableOptionLine.failableMap { failableBodyEither }

val SentenceLine.failableEither: Failable<Either>
	get() =
		failableSentence(eitherWord).failableMap { failableBodyEither }

fun Either.contains(otherWord: Word): Boolean =
	word == otherWord && script.patternOrNull == null

fun Either.contains(line: PatternLine): Boolean =
	word == line.word && script.patternOrNull != null && script.patternOrNull == line.pattern