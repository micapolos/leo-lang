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
import leo21.compiled.Compiled
import leo21.definition.Definitions
import leo21.evaluated.Evaluated
import leo21.evaluated.LineEvaluated
import leo21.evaluated.lineEvaluated
import leo21.token.body.EvaluatorNodeRepeatParent
import leo21.token.body.FunctionCompiler
import leo21.token.body.RepeatCompiler
import leo21.token.body.given
import leo21.token.define.DefineCompiler
import leo21.token.processor.DefineCompilerProcessor
import leo21.token.processor.EvaluatorProcessor
import leo21.token.processor.FunctionCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.RepeatCompilerProcessor
import leo21.token.processor.processor
import leo21.type.isEmpty

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
			"apply" ->
				EvaluatorProcessor(
					EvaluatorNode(
						EvaluatorNodeApplyEvaluatorParent(this),
						evaluator.begin))
			"define" ->
				if (evaluator.evaluated.type.isEmpty)
					DefineCompilerProcessor(
						DefineCompiler(
							DefineCompiler.Parent.Evaluator(this),
							evaluator.context.beginModule))
				else error { not { expected { word { define } } } }
			"do" ->
				EvaluatorProcessor(
					EvaluatorNode(
						EvaluatorNodeDoEvaluatorParent(EvaluatorNodeDo(this)),
						evaluator.beginDo))
			"repeat" ->
				RepeatCompilerProcessor(
					RepeatCompiler(
						EvaluatorNodeRepeatParent(this),
						evaluator.evaluated.type.given,
						evaluator.context.beginModule))
			"function" ->
				FunctionCompilerProcessor(
					FunctionCompiler(
						FunctionCompiler.Parent.Evaluator(this),
						evaluator.context.beginModule))
			else -> EvaluatorProcessor(begin(token.begin.string))
		}
		is EndToken -> parentOrNull?.end(evaluator.evaluated).orError { not { expected { end } } }
	}

fun EvaluatorNode.plus(line: LineEvaluated): EvaluatorNode =
	copy(evaluator = evaluator.plus(line))

fun EvaluatorNode.end(definitions: Definitions): EvaluatorNode =
	copy(evaluator = evaluator.plus(definitions))

fun EvaluatorNode.do_(rhs: Evaluated): EvaluatorNode =
	copy(evaluator = evaluator.do_(rhs))

fun EvaluatorNode.begin(name: String): EvaluatorNode =
	EvaluatorNode(
		EvaluatorNodeBeginEvaluatorParent(EvaluatorNodeBegin(this, name)),
		evaluator.begin)

fun EvaluatorNode.plus(arrowCompiled: ArrowCompiled): EvaluatorNode =
	copy(evaluator = evaluator.plus(arrowCompiled))

val EvaluatorNode.rootEvaluator: Evaluator
	get() =
		if (parentOrNull != null) null!!
		else evaluator

fun EvaluatorNode.apply(rhs: Evaluated): EvaluatorNode =
	copy(evaluator = evaluator.apply(rhs))

fun EvaluatorNode.repeat(compiled: Compiled): EvaluatorNode = TODO()