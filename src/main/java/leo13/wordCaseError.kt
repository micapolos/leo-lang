package leo13

data class WordCaseError(val error: SentenceWordError)

fun wordCaseError(error: SentenceWordError) = WordCaseError(error)
