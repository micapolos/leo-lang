package leo13

data class SentenceAny(val any: ObjectAny) {
	override fun toString() = sentenceAnySentenceWriter.toString(this)
}

fun sentence(any: ObjectAny) = SentenceAny(any)

val sentenceAnySentenceWriter =
	sentenceWriter<SentenceAny>(sentenceWord) { sentence(anyWord) }