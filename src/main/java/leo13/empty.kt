package leo13

val emptySentenceWriter: SentenceWriter<Empty> =
	sentenceWriter(emptyWord) { sentence(unitWord) }

object Empty {
	override fun toString() = emptySentenceWriter.toString(this)
}

val empty = Empty
