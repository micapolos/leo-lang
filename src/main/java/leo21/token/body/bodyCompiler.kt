package leo21.token.body

import leo14.Begin
import leo14.End
import leo14.Literal
import leo14.Token
import leo14.switch
import leo21.compiled.Compiled
import leo21.compiled.LineCompiled
import leo21.compiled.lineCompiled
import leo21.compiled.lineTo
import leo21.token.processor.DefineCompilerTokenProcessor
import leo21.token.processor.FunctionCompilerTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.token.processor.processor

data class BodyCompiler(
	val parentOrNull: Parent?,
	val body: Body
) {
	sealed class Parent {
		data class BodyName(val bodyCompiler: BodyCompiler, val name: String) : Parent()
		data class BodyDo(val bodyCompiler: BodyCompiler) : Parent()
		data class FunctionIt(val functionItCompiler: FunctionItCompiler) : Parent()
	}
}

val nullBodyCompilerParent: BodyCompiler.Parent? = null
val emptyBodyCompiler = nullBodyCompilerParent.compiler(emptyBody)

fun BodyCompiler.Parent?.compiler(body: Body) = BodyCompiler(this, body)
val BodyCompiler.doParent: BodyCompiler.Parent get() = BodyCompiler.Parent.BodyDo(this)
fun BodyCompiler.parent(name: String): BodyCompiler.Parent = BodyCompiler.Parent.BodyName(this, name)

fun BodyCompiler.plus(token: Token): TokenProcessor =
	token.switch(this::plus, this::plus, this::plus)

fun BodyCompiler.plus(literal: Literal): TokenProcessor =
	this
		.plus(lineCompiled(literal))
		.processor

fun BodyCompiler.plus(begin: Begin): TokenProcessor =
	when (begin.string) {
		"apply" -> TODO()
		"define" ->
			DefineCompilerTokenProcessor(
				DefineCompiler(
					DefineCompiler.Parent.Body(this),
					body.module))
		"function" ->
			FunctionCompilerTokenProcessor(
				FunctionCompiler(
					FunctionCompiler.Parent.Body(this),
					body.module))
		"do" -> this
			.doParent
			.compiler(body.beginDo)
			.processor
		else ->
			this
				.parent(begin.string)
				.compiler(body.begin)
				.processor
	}

fun BodyCompiler.plus(end: End): TokenProcessor =
	parentOrNull!!.process(body)

fun BodyCompiler.Parent.process(body: Body): TokenProcessor =
	when (this) {
		is BodyCompiler.Parent.BodyName ->
			bodyCompiler
				.plus(name lineTo body.wrapCompiled)
				.processor
		is BodyCompiler.Parent.BodyDo ->
			bodyCompiler
				.plusDo(body)
				.processor
		is BodyCompiler.Parent.FunctionIt ->
			functionItCompiler.plus(body.wrapCompiled)
	}

fun BodyCompiler.plus(lineCompiled: LineCompiled): BodyCompiler =
	copy(body = body.plus(lineCompiled))

fun BodyCompiler.set(compiled: Compiled): BodyCompiler =
	copy(body = body.set(compiled))

fun BodyCompiler.plus(definitions: Definitions): BodyCompiler =
	copy(body = body.plus(definitions))

fun BodyCompiler.plusDo(rhsBody: Body): BodyCompiler =
	copy(body = body.do_(rhsBody))
