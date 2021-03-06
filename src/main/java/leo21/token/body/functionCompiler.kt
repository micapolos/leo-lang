package leo21.token.body

import leo.base.notNullIf
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.ScriptLine
import leo14.Scriptable
import leo14.Token
import leo14.anyOptionalReflectScriptLine
import leo14.dsl.x
import leo14.end
import leo14.error
import leo14.lambda.abstraction
import leo14.lambda.fn
import leo14.lineTo
import leo14.orError
import leo14.script
import leo14.token
import leo15.dsl.*
import leo21.compiled.ArrowCompiled
import leo21.compiled.Compiled
import leo21.compiled.compiled
import leo21.compiled.line
import leo21.compiled.of
import leo21.definition.functionDefinition
import leo21.token.define.DefineCompiler
import leo21.token.define.plus
import leo21.token.evaluator.EvaluatorNode
import leo21.token.evaluator.plus
import leo21.token.processor.BodyCompilerProcessor
import leo21.token.processor.DefineCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.TypeCompilerProcessor
import leo21.token.processor.processor
import leo21.token.type.compiler.FunctionCompilerTypeParent
import leo21.token.type.compiler.FunctionToCompilerTypeParent
import leo21.token.type.compiler.TypeCompiler
import leo21.token.type.compiler.plus
import leo21.type.Type
import leo21.type.arrowTo
import leo21.type.printScript
import leo21.type.type

data class FunctionCompiler(
	val parentOrNull: Parent?,
	val module: Module,
	val type: Type = type(),
	val toTypeOrNull: Type? = null
) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "function" lineTo script(
			parentOrNull.anyOptionalReflectScriptLine("parent"),
			module.reflectScriptLine,
			type.reflectScriptLine,
			"to" lineTo script(toTypeOrNull.anyOptionalReflectScriptLine("type")))

	sealed class Parent : Scriptable() {
		override val reflectScriptLine: ScriptLine
			get() = "parent" lineTo script(
				when (this) {
					is Define -> defineCompiler.reflectScriptLine
					is Body -> bodyCompiler.reflectScriptLine
					is Evaluator -> evaluatorNode.reflectScriptLine
				}
			)

		data class Define(val defineCompiler: DefineCompiler) : Parent() {
			override fun toString() = super.toString()
		}

		data class Body(val bodyCompiler: BodyCompiler) : Parent() {
			override fun toString() = super.toString()
		}

		data class Evaluator(val evaluatorNode: EvaluatorNode) : Parent() {
			override fun toString() = super.toString()
		}
	}
}

data class FunctionDoesCompiler(
	val parentOrNull: FunctionCompiler.Parent?,
	val arrowCompiled: ArrowCompiled
)

fun FunctionCompiler.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> null
		is BeginToken ->
			when (token.begin.string) {
				"does" ->
					BodyCompilerProcessor(
						BodyCompiler(
							BodyCompiler.Parent.FunctionDoes(this),
							module.begin(type.given).body(compiled())))
				"to" ->
					if (toTypeOrNull != null) error { not { expected { word { to } } } }
					else TypeCompilerProcessor(
						TypeCompiler(
							FunctionToCompilerTypeParent(this),
							module.lines))
				else ->
					if (toTypeOrNull != null) error { expected { word { does } } }
					else null
			}
		is EndToken -> error { expected { type.or { word { does } } } }
	} ?: TypeCompiler(
		FunctionCompilerTypeParent(this),
		module.lines,
		type,
		autoEnd = true)
		.plus(token)

fun FunctionDoesCompiler.plus(token: Token): Processor =
	notNullIf(token == token(end)) {
		parentOrNull!!.plus(arrowCompiled)
	}.orError { expected { end } }

fun FunctionCompiler.Parent.plus(arrowCompiled: ArrowCompiled): Processor =
	when (this) {
		is FunctionCompiler.Parent.Define ->
			DefineCompilerProcessor(
				defineCompiler.plus(
					functionDefinition(
						arrowCompiled.arrow.lhs,
						arrowCompiled.term.abstraction { it }.of(arrowCompiled.arrow.rhs))))
		is FunctionCompiler.Parent.Body ->
			BodyCompilerProcessor(bodyCompiler.plus(line(arrowCompiled)))
		is FunctionCompiler.Parent.Evaluator ->
			evaluatorNode.plus(arrowCompiled).processor
	}

fun FunctionCompiler.plus(type: Type): Processor =
	FunctionCompiler(parentOrNull, module, type).processor

fun FunctionCompiler.plus(compiled: Compiled): Processor =
	if (toTypeOrNull != null && toTypeOrNull != compiled.type)
		error { expected { x(toTypeOrNull.printScript) } }
	else FunctionDoesCompiler(parentOrNull, fn(compiled.term).of(type arrowTo compiled.type))
		.processor

fun FunctionCompiler.plusTo(type: Type): FunctionCompiler =
	copy(toTypeOrNull = type)