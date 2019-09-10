package leo13

interface SentenceWriter<in V> {
	val word: Word
	fun bodySentence(value: V): Sentence

	fun sentenceLine(value: V): SentenceLine =
		word lineTo bodySentence(value)
}