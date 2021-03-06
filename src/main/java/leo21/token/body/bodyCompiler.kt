package leo21.token.body

import leo.base.notNullIf
import leo13.stack
import leo14.Begin
import leo14.End
import leo14.Literal
import leo14.ScriptLine
import leo14.Scriptable
import leo14.Token
import leo14.anyOptionalReflectScriptLine
import leo14.anyReflectScriptLine
import leo14.lineTo
import leo14.script
import leo14.switch
import leo21.compiled.Compiled
import leo21.compiled.LineCompiled
import leo21.compiled.lineCompiled
import leo21.compiled.lineTo
import leo21.compiled.switch
import leo21.definition.Definitions
import leo21.token.define.DefineCompiler
import leo21.token.evaluator.EvaluatorNode
import leo21.token.evaluator.repeat
import leo21.token.processor.BodyCompilerProcessor
import leo21.token.processor.DefineCompilerProcessor
import leo21.token.processor.FunctionCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.RepeatCompilerProcessor
import leo21.token.processor.SwitchCompilerProcessor
import leo21.token.processor.processor
import leo21.type.Line
import leo21.type.type

data class BodyCompiler(
	val parentOrNull: Parent?,
	val body: Body
) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "body" lineTo script(
			parentOrNull.anyOptionalReflectScriptLine("parent"),
			body.anyReflectScriptLine)

	sealed class Parent : Scriptable() {
		override val reflectScriptLine: ScriptLine
			get() = "parent" lineTo script(
				when (this) {
					is BodyName -> "field" lineTo script(bodyCompiler.reflectScriptLine, "name" lineTo script(name))
					is BodyDo -> "do" lineTo script(bodyCompiler.reflectScriptLine)
					is BodyRepeat -> "repeat" lineTo script(bodyCompiler.reflectScriptLine)
					is BodyApply -> "apply" lineTo script(bodyCompiler.reflectScriptLine)
					is FunctionDoes -> functionCompiler.anyReflectScriptLine
					is SwitchCase -> "switch" lineTo script(switchCompiler.anyReflectScriptLine, case.anyReflectScriptLine)
					is EvaluatorRepeat -> "repeat" lineTo script(evaluatorNode.reflectScriptLine)
				}
			)

		data class BodyName(val bodyCompiler: BodyCompiler, val name: String) : Parent() {
			override fun toString() = super.toString()
		}

		data class BodyDo(val bodyCompiler: BodyCompiler) : Parent() {
			override fun toString() = super.toString()
		}

		data class BodyRepeat(val bodyCompiler: BodyCompiler) : Parent() {
			override fun toString() = super.toString()
		}

		data class EvaluatorRepeat(val evaluatorNode: EvaluatorNode) : Parent() {
			override fun toString() = super.toString()
		}

		data class BodyApply(val bodyCompiler: BodyCompiler) : Parent() {
			override fun toString() = super.toString()
		}

		data class FunctionDoes(val functionCompiler: FunctionCompiler) : Parent() {
			override fun toString() = super.toString()
		}

		data class SwitchCase(val switchCompiler: SwitchCompiler, val case: Line) : Parent() {
			override fun toString() = super.toString()
		}
	}
}

val nullBodyCompilerParent: BodyCompiler.Parent? = null
val emptyBodyCompiler = nullBodyCompilerParent.compiler(emptyBody)

fun BodyCompiler.Parent?.compiler(body: Body) = BodyCompiler(this, body)
val BodyCompiler.doParent: BodyCompiler.Parent get() = BodyCompiler.Parent.BodyDo(this)
fun BodyCompiler.parent(name: String): BodyCompiler.Parent = BodyCompiler.Parent.BodyName(this, name)

fun BodyCompiler.plus(token: Token): Processor =
	token.switch(this::plus, this::plus, this::plus)

fun BodyCompiler.plus(literal: Literal): Processor =
	this
		.plus(lineCompiled(literal))
		.processor

fun BodyCompiler.plus(begin: Begin): Processor =
	when (begin.string) {
		"apply" ->
			BodyCompilerProcessor(
				BodyCompiler(
					BodyCompiler.Parent.BodyApply(this),
					body.begin))
		"define" ->
			DefineCompilerProcessor(
				DefineCompiler(
					DefineCompiler.Parent.Body(this),
					body.module.begin))
		"do" -> this
			.doParent
			.compiler(body.beginDo)
			.processor
		"repeat" ->
			RepeatCompilerProcessor(
				RepeatCompiler(
					BodyCompilerRepeatParent(this),
					body.compiled.type.given,
					body.module.begin))
		"function" ->
			FunctionCompilerProcessor(
				FunctionCompiler(
					FunctionCompiler.Parent.Body(this),
					body.module,
					type()))
		"switch" ->
			SwitchCompilerProcessor(
				SwitchCompiler(
					this,
					body.module,
					stack(),
					body.compiled.switch))
		else ->
			this
				.parent(begin.string)
				.compiler(body.begin)
				.processor
	}

fun BodyCompiler.plus(end: End): Processor =
	parentOrNull!!.process(body.wrapCompiled)

fun BodyCompiler.Parent.process(compiled: Compiled): Processor =
	when (this) {
		is BodyCompiler.Parent.BodyName ->
			bodyCompiler.plus(name lineTo compiled).processor
		is BodyCompiler.Parent.BodyDo ->
			bodyCompiler.plusDo(compiled).processor
		is BodyCompiler.Parent.BodyRepeat ->
			bodyCompiler.plusDo(compiled).processor
		is BodyCompiler.Parent.BodyApply ->
			bodyCompiler.apply(compiled).processor
		is BodyCompiler.Parent.FunctionDoes ->
			functionCompiler.plus(compiled)
		is BodyCompiler.Parent.SwitchCase ->
			switchCompiler.plus(case, compiled)
		is BodyCompiler.Parent.EvaluatorRepeat ->
			evaluatorNode.repeat(compiled).processor
	}

fun BodyCompiler.plus(lineCompiled: LineCompiled): BodyCompiler =
	copy(body = body.plus(lineCompiled))

fun BodyCompiler.set(compiled: Compiled): BodyCompiler =
	copy(body = body.set(compiled))

fun BodyCompiler.plus(definitions: Definitions): BodyCompiler =
	copy(body = body.plus(definitions))

fun BodyCompiler.plusDo(compiled: Compiled): BodyCompiler =
	copy(body = body.do_(compiled))

fun BodyCompiler.apply(compiled: Compiled): BodyCompiler =
	copy(body = body.apply(compiled))

val BodyCompiler.rootBody: Body get() = notNullIf(parentOrNull == null) { body }!!