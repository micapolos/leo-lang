package leo13

data class SentenceWriterField<in V, F>(val writer: SentenceWriter<F>, val fn: V.() -> F)

fun <V, F> field(writer: SentenceWriter<F>, fn: V.() -> F) = SentenceWriterField(writer, fn)

fun <V, F> SentenceWriterField<V, F>.sentenceLine(value: V): SentenceLine =
	writer.sentenceLine(value.fn())