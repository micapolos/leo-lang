package leo21.token.evaluator

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.ScriptLine
import leo14.Scriptable
import leo14.Token
import leo14.anyOptionalReflectScriptLine
import leo14.error
import leo14.lineTo
import leo14.orError
import leo14.script
import leo15.dsl.*
import leo21.compiled.ArrowCompiled
import leo21.compiled.line
import leo21.compiled.of
import leo21.evaluator.LineEvaluated
import leo21.evaluator.lineEvaluated
import leo21.evaluator.of
import leo21.prim.runtime.value
import leo21.token.body.DefineCompiler
import leo21.token.body.FunctionCompiler
import leo21.token.body.Module
import leo21.token.processor.DefineCompilerProcessor
import leo21.token.processor.EvaluatorProcessor
import leo21.token.processor.FunctionCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.processor
import leo21.type.isEmpty
import leo21.type.type

data class EvaluatorNode(
	val parentOrNull: EvaluatorParent?,
	val evaluator: Evaluator
) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "node" lineTo script(
			parentOrNull.anyOptionalReflectScriptLine("parent"),
			evaluator.reflectScriptLine)
}

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
			"function" -> FunctionCompilerProcessor(
				FunctionCompiler(
					FunctionCompiler.Parent.Evaluator(this),
					evaluator.context.beginModule,
					type()))
			else -> EvaluatorProcessor(begin(token.begin.string))
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

fun EvaluatorNode.begin(name: String): EvaluatorNode =
	EvaluatorNode(
		EvaluatorNodeBeginEvaluatorParent(EvaluatorNodeBegin(this, name)),
		evaluator.begin)

fun EvaluatorNode.plus(arrowCompiled: ArrowCompiled): EvaluatorNode =
	copy(evaluator = evaluator.plus(arrowCompiled))