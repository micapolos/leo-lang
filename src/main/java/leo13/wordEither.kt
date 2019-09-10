package leo13

data class WordEither(val word: Word) {
	override fun toString() = wordEitherSentenceWriter.toString(this)
}

fun either(word: Word) = WordEither(word)

val wordEitherSentenceWriter =
	sentenceWriter<WordEither>(eitherWord) { sentence(word) }

val wordEitherSentenceStartWriter =
	sentenceStartWriter<WordEither> {
		start(eitherWord lineTo sentence(word))
	}

fun WordEither.matches(word: Word): Boolean =
	this.word == word

fun WordEither.contains(either: WordEither): Boolean =
	word == either.word
