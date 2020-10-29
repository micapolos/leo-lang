package leo20.tokenizer

import leo14.BeginToken
import leo14.EndToken
import leo14.Literal
import leo14.LiteralToken
import leo14.Script
import leo14.Token
import leo14.script
import leo20.Line
import leo20.body
import leo20.function
import leo20.line

data class Evaluator(
	val parent: EvaluatorParent?,
	val evaluated: Evaluated
)

fun Evaluator.push(token: Token): Tokenizer? =
	when (token) {
		is LiteralToken -> EvaluatorTokenizer(push(token.literal))
		is BeginToken -> begin(token.begin.string)
		is EndToken -> parent?.end(evaluated)
	}

fun Evaluator.push(literal: Literal): Evaluator =
	evaluated.push(literal).run { copy(evaluated = this) }

fun Evaluator.begin(name: String): Tokenizer =
	when (name) {
		"function" -> WriterTokenizer(Writer(FunctionWriterParent(this), script()))
		else -> EvaluatorTokenizer(beginResolve(name))
	}

fun Evaluator.beginResolve(name: String): Evaluator =
	Evaluator(ResolveEvaluatorParent(name, this), evaluated.begin)

fun Evaluator.pushFunction(script: Script): Evaluator =
	push(line(evaluated.bindings.function(body(script))))

fun Evaluator.push(line: Line): Evaluator =
	copy(evaluated = evaluated.plus(line))
