package leo13

interface SentenceStartWriter<in V> {
	fun sentenceStart(value: V): SentenceStart
}

fun <V> sentenceStartWriter(fn: V.() -> SentenceStart) =
	object : SentenceStartWriter<V> {
		override fun sentenceStart(value: V) = value.fn()
	}

fun <V> startWriter(writer: SentenceLineWriter<V>): SentenceStartWriter<V> =
	sentenceStartWriter {
		start(writer.sentenceLine(this))
	}