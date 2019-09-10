package leo13

data class WordCase(val word: Word)

fun case(word: Word) = WordCase(word)

val WordCase.sentenceLine: SentenceLine
	get() =
		caseWord lineTo sentence(word)
