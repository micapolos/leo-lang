package leo13

data class SentenceWordError(val sentence: Sentence)

fun sentenceWordError(sentence: Sentence) = SentenceWordError(sentence)
