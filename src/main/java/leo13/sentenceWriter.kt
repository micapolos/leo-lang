package leo13

interface SentenceWriter<in V> {
	val word: Word
	fun bodySentence(value: V): Sentence
	fun sentenceLine(value: V): SentenceLine = word lineTo bodySentence(value)
	fun sentence(value: V): Sentence = sentence(sentenceLine(value))
	fun toString(value: V): String = sentenceLine(value).toString()
}

fun <V> sentenceWriter(word: Word, bodySentenceFn: V.() -> Sentence): SentenceWriter<V> =
	object : SentenceWriter<V> {
		override val word: Word get() = word
		override fun bodySentence(value: V): Sentence = value.bodySentenceFn()
	}

fun <V> writer(word: Word, writer: SentenceWriter<V>): SentenceWriter<V> =
	sentenceWriter(word) {
		sentence(writer.sentenceLine(this))
	}

fun <V> recursiveWriter(fn: () -> SentenceWriter<V>): SentenceWriter<V> =
	object : SentenceWriter<V> {
		override val word get() = fn().word
		override fun bodySentence(value: V) = fn().bodySentence(value)
	}

fun <V, F1, F2> writer(
	word: Word,
	field1: SentenceWriterField<V, F1>,
	field2: SentenceWriterField<V, F2>): SentenceWriter<V> =
	sentenceWriter(word) {
		sentence(
			field1.sentenceLine(this),
			field2.sentenceLine(this))
	}

fun <V, V1, V2> sealedSentenceWriter(
	word: Word,
	writer1: SentenceWriter<V1>,
	writer2: SentenceWriter<V2>,
	fn: V.(SentenceWriter<V1>, SentenceWriter<V2>) -> Sentence) =
	sentenceWriter<V>(word) {
		fn(writer1, writer2)
	}

fun <V, V1, V2, V3, V4> sealedSentenceWriter(
	word: Word,
	writer1: SentenceWriter<V1>,
	writer2: SentenceWriter<V2>,
	writer3: SentenceWriter<V3>,
	writer4: SentenceWriter<V4>,
	fn: V.(
		V1.() -> Sentence,
		V2.() -> Sentence,
		V3.() -> Sentence,
		V4.() -> Sentence
	) -> Sentence) =
	sentenceWriter<V>(word) {
		fn(
			{ writer1.bodySentence(this) },
			{ writer2.bodySentence(this) },
			{ writer3.bodySentence(this) },
			{ writer4.bodySentence(this) })
	}
