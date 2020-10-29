package leo20.tokenizer

import leo14.Script
import leo14.lineTo

sealed class WriterParent
data class FunctionWriterParent(val evaluator: Evaluator) : WriterParent()
data class TestWriterParent(val evaluator: Evaluator) : WriterParent()
data class PlusWriterParent(val name: String, val writer: Writer) : WriterParent()

fun WriterParent.end(script: Script): Tokenizer =
	when (this) {
		is FunctionWriterParent -> EvaluatorTokenizer(evaluator.pushFunction(script))
		is PlusWriterParent -> WriterTokenizer(writer.push(name lineTo script))
		is TestWriterParent -> TODO()
	}
