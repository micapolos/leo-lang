package leo13

abstract class SentenceObject<V: SentenceObject<V>> {
	abstract val sentenceWriter: SentenceWriter<V>

	@Suppress("UNCHECKED_CAST")
	val sentenceValue: V get() = this as V

	override fun toString() = sentenceWriter.sentenceLine(sentenceValue).toString()
}
