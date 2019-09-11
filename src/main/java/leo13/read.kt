package leo13

sealed class Read<out V> {
	abstract fun <R> map(fn: V.() -> R): Read<R>
	abstract fun <R> readMap(fn: V.() -> Read<R>): Read<R>
}

data class ValueRead<V>(val value: V): Read<V>() {
	override fun <R> map(fn: V.() -> R): Read<R> = read(value.fn())
	override fun <R> readMap(fn: V.() -> Read<R>): Read<R> = value.fn()
}

data class ErrorRead<V>(val sentence: Sentence): Read<V>() {
	override fun <R> map(fn: V.() -> R): Read<R> = errorRead(sentence)
	override fun <R> readMap(fn: V.() -> Read<R>): Read<R> = errorRead(sentence)
}

fun <V> read(value: V): Read<V> = ValueRead(value)
fun <V> errorRead(sentence: Sentence): Read<V> = ErrorRead(sentence)
fun <V> errorRead(sentenceLine: SentenceLine): Read<V> = errorRead(sentence(sentenceLine))
fun <V> errorRead(word: Word): Read<V> = errorRead(sentence(word))

fun <V: Any> V?.nonNullRead(sentence: Sentence): Read<V> =
	if (this == null) errorRead(expectedWord lineTo sentence)
	else read(this)

val Sentence.wordRead: Read<Word> get() =
	wordOrNull.nonNullRead(sentence(wordWord))

val Sentence.lineRead: Read<SentenceLine> get() =
	lineOrNull.nonNullRead(sentence(lineWord))

val Sentence.linkRead: Read<SentenceLink> get() =
	linkOrNull.nonNullRead(sentence(linkWord))

fun Word.unitRead(word: Word): Read<Unit> =
	if (this != expectedWord) errorRead(sentence(word))
	else read(Unit)

fun SentenceLine.sentenceRead(word: Word): Read<Sentence> =
	if (this.word != word) errorRead(wordWord lineTo sentence(word))
	else read(sentence)
