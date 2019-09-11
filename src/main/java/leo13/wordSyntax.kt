package leo13

data class WordSyntax(val word: Word)

fun syntax(word: Word) = WordSyntax(word)

val Sentence.wordSyntaxResult: Result<WordSyntax, SyntaxError>
	get() =
		wordOrNull
			?.let { successResult(syntax(it)) }
			?: failureResult(onlyWordExpectedSyntaxError)