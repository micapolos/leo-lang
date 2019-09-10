package leo13

abstract class SentenceObject<V : SentenceObject<V>> {
	override fun toString() = sentenceObjectWriter.toString(sentenceObjectValue)

	abstract val sentenceObjectWriter: SentenceWriter<V>

	@Suppress("UNCHECKED_CAST")
	private val sentenceObjectValue: V
		get() = this as V
}
