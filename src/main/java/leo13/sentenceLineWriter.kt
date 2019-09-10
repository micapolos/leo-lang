package leo13

interface SentenceLineWriter<in V> {
	val word: Word
	fun bodySentence(value: V): Sentence
	fun sentenceLine(value: V): SentenceLine = word lineTo bodySentence(value)
	fun sentence(value: V): Sentence = sentence(sentenceLine(value))
	fun toString(value: V): String = sentenceLine(value).toString()
}

fun <V> sentenceWriter(word: Word, bodySentenceFn: V.() -> Sentence): SentenceLineWriter<V> =
	object : SentenceLineWriter<V> {
		override val word: Word get() = word
		override fun bodySentence(value: V): Sentence = value.bodySentenceFn()
	}

fun <V> writer(word: Word, writer: SentenceLineWriter<V>): SentenceLineWriter<V> =
	sentenceWriter(word) {
		sentence(writer.sentenceLine(this))
	}

fun <V> recursiveWriter(fn: () -> SentenceLineWriter<V>): SentenceLineWriter<V> =
	object : SentenceLineWriter<V> {
		override val word get() = fn().word
		override fun bodySentence(value: V) = fn().bodySentence(value)
	}

fun <V, F1, F2> writer(
	word: Word,
	field1: SentenceWriterField<V, F1>,
	field2: SentenceWriterField<V, F2>): SentenceLineWriter<V> =
	sentenceWriter(word) {
		sentence(
			field1.sentenceLine(this),
			field2.sentenceLine(this))
	}

fun <V, V1, V2> sealedWriter(
	word: Word,
	writer1: SentenceLineWriter<V1>,
	writer2: SentenceLineWriter<V2>,
	fn: V.(V1.() -> Sentence, V2.() -> Sentence) -> Sentence) =
	sentenceWriter<V>(word) {
		fn(
			{ writer1.sentence(this) },
			{ writer2.sentence(this) })
	}

fun <V, V1, V2, V3, V4> sealedWriter(
	word: Word,
	writer1: SentenceLineWriter<V1>,
	writer2: SentenceLineWriter<V2>,
	writer3: SentenceLineWriter<V3>,
	writer4: SentenceLineWriter<V4>,
	fn: V.(
		V1.() -> Sentence,
		V2.() -> Sentence,
		V3.() -> Sentence,
		V4.() -> Sentence
	) -> Sentence) =
	sentenceWriter<V>(word) {
		fn(
			{ writer1.sentence(this) },
			{ writer2.sentence(this) },
			{ writer3.sentence(this) },
			{ writer4.sentence(this) })
	}
