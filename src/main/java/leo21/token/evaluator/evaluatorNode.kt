package leo21.token.evaluator

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.error
import leo14.orError
import leo15.dsl.*
import leo21.evaluator.LineEvaluated
import leo21.evaluator.lineEvaluated
import leo21.token.body.DefineCompiler
import leo21.token.body.Module
import leo21.token.processor.DefineCompilerProcessor
import leo21.token.processor.EvaluatorProcessor
import leo21.token.processor.Processor
import leo21.token.processor.processor
import leo21.type.isEmpty

data class EvaluatorNode(
	val parentOrNull: EvaluatorParent?,
	val evaluator: Evaluator
)

val emptyEvaluatorNode = EvaluatorNode(null, emptyEvaluator)

fun EvaluatorNode.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> plus(token.literal.lineEvaluated).processor
		is BeginToken -> when (token.begin.string) {
			"define" ->
				if (evaluator.evaluated.type.isEmpty)
					DefineCompilerProcessor(
						DefineCompiler(
							DefineCompiler.Parent.Evaluator(this),
							evaluator.context.beginModule))
				else error { not { expected { word { define } } } }
			"do" -> EvaluatorProcessor(
				EvaluatorNode(
					EvaluatorNodeDoEvaluatorParent(EvaluatorNodeDo(this)),
					evaluator.beginDo))
			else -> EvaluatorProcessor(
				EvaluatorNode(
					EvaluatorNodeBeginEvaluatorParent(EvaluatorNodeBegin(this, token.begin.string)),
					evaluator.begin))
		}
		is EndToken -> parentOrNull?.end(evaluator).orError { not { expected { end } } }
	}

fun EvaluatorNode.plus(line: LineEvaluated): EvaluatorNode =
	copy(evaluator = evaluator.plus(line))

fun EvaluatorNode.end(field: EvaluatorField): EvaluatorNode =
	copy(evaluator = evaluator.plus(field))

fun EvaluatorNode.end(module: Module): EvaluatorNode =
	copy(evaluator = evaluator.plus(module))

fun EvaluatorNode.do_(evaluator: Evaluator): EvaluatorNode =
	copy(evaluator = evaluator.do_(evaluator))