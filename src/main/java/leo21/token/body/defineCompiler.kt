package leo21.token.body

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.ScriptLine
import leo14.Scriptable
import leo14.Token
import leo14.anyOptionalReflectScriptLine
import leo14.anyReflectScriptLine
import leo14.error
import leo14.lineTo
import leo14.orError
import leo14.script
import leo15.dsl.*
import leo21.definition.Definition
import leo21.token.evaluator.EvaluatorNode
import leo21.token.evaluator.end
import leo21.token.processor.FunctionCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.TypeCompilerProcessor
import leo21.token.processor.processor
import leo21.token.type.compiler.DefineCompilerTypeParent
import leo21.token.type.compiler.TypeCompiler
import leo21.type.type

data class DefineCompiler(
	val parentOrNull: Parent?,
	val module: Module
) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "define" lineTo script(
			parentOrNull.anyOptionalReflectScriptLine("parent"),
			module.anyReflectScriptLine)

	sealed class Parent : Scriptable() {
		override val reflectScriptLine: ScriptLine
			get() = "parent" lineTo script(
				when (this) {
					is Body -> bodyCompiler.anyReflectScriptLine
					is Evaluator -> evaluatorNode.anyReflectScriptLine
				}
			)

		data class Body(val bodyCompiler: BodyCompiler) : Parent() {
			override fun toString() = super.toString()
		}

		data class Evaluator(val evaluatorNode: EvaluatorNode) : Parent() {
			override fun toString() = super.toString()
		}
	}
}

val emptyDefineCompiler = DefineCompiler(null, emptyModule)

fun DefineCompiler.plus(token: Token): Processor =
	when (token) {
		is LiteralToken ->
			error {
				expected {
					one {
						of {
							word { function }
							word { type }
						}
					}
				}
			}
		is BeginToken -> when (token.begin.string) {
			"constant" -> error { did { not { implement } } }
			"function" ->
				FunctionCompilerProcessor(
					FunctionCompiler(
						FunctionCompiler.Parent.Define(this),
						module,
						type()))
			"type" ->
				TypeCompilerProcessor(
					TypeCompiler(
						DefineCompilerTypeParent(this),
						module.lines,
						type()))
			else ->
				error {
					expected {
						one {
							of {
								word { function }
								word { type }
							}
						}
					}
				}
		}
		is EndToken -> parentOrNull?.plus(module).orError { not { expected { end } } }
	}

fun DefineCompiler.plus(definition: Definition): DefineCompiler =
	copy(module = module.plus(definition))

fun DefineCompiler.Parent.plus(module: Module): Processor =
	when (this) {
		is DefineCompiler.Parent.Body -> bodyCompiler.plus(module).processor
		is DefineCompiler.Parent.Evaluator -> evaluatorNode.end(module).processor
	}
