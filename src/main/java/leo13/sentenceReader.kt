package leo13

interface SentenceReader<out V> {
	val word: Word

	fun readBody(sentence: Sentence): Read<V>

	fun read(line: SentenceLine): Read<V> =
		if (word != line.word) errorRead(wordWord)
		else readBody(line.sentence)

	fun read(sentence: Sentence): Read<V> =
		sentence.lineRead.readMap { read(this) }
}

fun <V> sentenceReader(word: Word, readBodyFn: Sentence.() -> Read<V>) =
	object : SentenceReader<V> {
		override val word = word
		override fun readBody(sentence: Sentence) = sentence.readBodyFn()
	}