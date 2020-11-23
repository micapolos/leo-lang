package leo21.token.body

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.error
import leo15.dsl.*
import leo21.compiled.compiled
import leo21.token.evaluator.EvaluatorNode
import leo21.token.processor.BodyCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.TypeCompilerProcessor
import leo21.token.type.compiler.RepeatDoingCompilerTypeParent
import leo21.token.type.compiler.TypeCompiler
import leo21.type.Type
import leo21.type.type

data class RepeatCompiler(
	val parent: RepeatParent,
	val given: Given,
	val module: Module
)

sealed class RepeatParent
data class BodyCompilerRepeatParent(val bodyCompiler: BodyCompiler) : RepeatParent()
data class EvaluatorNodeRepeatParent(val evaluatorNode: EvaluatorNode) : RepeatParent()

fun RepeatCompiler.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> error { expected { word { doing } } }
		is BeginToken ->
			when (token.begin.string) {
				"doing" -> TypeCompilerProcessor(
					TypeCompiler(
						RepeatDoingCompilerTypeParent(this),
						module.lines,
						type()))
				else -> error { expected { word { doing } } }
			}
		is EndToken -> error { expected { word { doing } } }
	}

fun RepeatCompiler.plusDoing(type: Type): Processor =
	BodyCompilerProcessor(
		BodyCompiler(
			parent.bodyCompilerParent,
			Body(
				module.plus(given.type.functionBinding(type)).plus(given.binding),
				compiled())))

val RepeatParent.bodyCompilerParent: BodyCompiler.Parent
	get() =
		when (this) {
			is BodyCompilerRepeatParent -> BodyCompiler.Parent.BodyRepeat(bodyCompiler)
			is EvaluatorNodeRepeatParent -> BodyCompiler.Parent.EvaluatorRepeat(evaluatorNode)
		}
