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
		eitherWord lineTo bodySentenceScriptLine.sentence

val Either.bodySentenceScriptLine: SentenceScriptLine
	get() =
		word lineTo script.bodySentenceScript

val SentenceScriptLine.failableBodyEither: Failable<Either>
	get() =
		if (script.sentenceOrNull == null) success(either(word))
		else script.sentenceOrNull.failableBodyPattern.map { word eitherTo script(this) }

val Sentence.failableBodyEither: Failable<Either>
	get() =
		failableScriptLine.failableMap { failableBodyEither }

val SentenceLine.failableEither: Failable<Either>
	get() =
		failableSentence(eitherWord).failableMap { failableBodyEither }

fun Either.contains(otherWord: Word): Boolean =
	word == otherWord && script.patternOrNull == null

fun Either.contains(line: PatternLine): Boolean =
	word == line.word && script.patternOrNull != null && script.patternOrNull == line.pattern