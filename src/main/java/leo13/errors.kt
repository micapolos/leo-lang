package leo13

var errorSentenceScript: SentenceScript = sentenceScript()

fun errorWrite(line: SentenceLine) {
	errorSentenceScript = errorSentenceScript.plus(line)
}

fun <V : Any> nullWithError(line: SentenceLine): V? {
	errorWrite(line)
	return null
}
