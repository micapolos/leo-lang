package leo13

data class WordCase(val word: Word)

fun case(word: Word) = WordCase(word)

val WordCase.sentenceLine: SentenceLine
	get() =
		caseWord lineTo sentence(word)

val SentenceLine.wordCaseRead: Read<WordCase>
	get() =
		sentenceRead(caseWord)
			.readMap { wordRead }
			.map { case(this) }

fun wordCaseResult(sentence: Sentence): Result<WordCase, WordCaseError> =
	sentence
		.wordResult
		.mapSuccess { case(this) }
		.mapFailure { wordCaseError(this) }
