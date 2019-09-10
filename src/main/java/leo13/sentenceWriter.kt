package leo13

interface SentenceWriter<in V> {
	val word: Word
	fun writeBody(value: V): Sentence

	fun sentenceLine(value: V): SentenceLine =
		word lineTo writeBody(value)

	fun sentence(value: V): Sentence =
		sentence(sentenceLine(value))
}

fun <V> sentenceWriter(word: Word, bodySentenceFn: V.() -> Sentence): SentenceWriter<V> =
	object : SentenceWriter<V> {
		override val word: Word get() = word
		override fun writeBody(value: V): Sentence = value.bodySentenceFn()
	}
