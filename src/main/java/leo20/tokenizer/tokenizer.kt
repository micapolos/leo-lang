package leo20.tokenizer

import leo14.Token

sealed class Tokenizer
data class EvaluatorTokenizer(val evaluator: Evaluator) : Tokenizer()
data class WriterTokenizer(val writer: Writer) : Tokenizer()

fun Tokenizer.push(token: Token): Tokenizer? =
	when (this) {
		is EvaluatorTokenizer -> evaluator.push(token)
		is WriterTokenizer -> writer.push(token)
	}
