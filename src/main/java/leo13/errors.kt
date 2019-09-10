package leo13

var errorSentenceScript: SentenceOption = sentenceOption()

fun errorWrite(line: SentenceLine) {
	errorSentenceScript = errorSentenceScript.plus(line)
}

fun <V : Any> nullWithError(line: SentenceLine): V? {
	errorWrite(line)
	return null
}
