package leo21.token.body

import leo.base.notNullIf
import leo14.Token
import leo14.begin
import leo14.end
import leo14.lambda.fn
import leo14.token
import leo21.compiled.ArrowCompiled
import leo21.compiled.Compiled
import leo21.compiled.compiled
import leo21.compiled.lineCompiled
import leo21.compiled.of
import leo21.token.processor.BodyCompilerTokenProcessor
import leo21.token.processor.DefineCompilerTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.token.processor.TypeCompilerTokenProcessor
import leo21.token.processor.asTokenProcessor
import leo21.token.type.compiler.FunctionCompilerTypeParent
import leo21.token.type.compiler.TypeCompiler
import leo21.type.Type
import leo21.type.arrowTo
import leo21.type.type

data class FunctionCompiler(
	val parentOrNull: Parent?,
	val module: Module
) {

	sealed class Parent {
		data class Define(val defineCompiler: DefineCompiler) : Parent()
		data class Body(val bodyCompiler: BodyCompiler) : Parent()
	}
}

data class FunctionItCompiler(
	val parentOrNull: FunctionCompiler.Parent?,
	val module: Module,
	val type: Type
)

data class FunctionItDoesCompiler(
	val parentOrNull: FunctionCompiler.Parent?,
	val arrowCompiled: ArrowCompiled
)

fun FunctionCompiler.plus(token: Token): TokenProcessor =
	notNullIf(token == token(begin("it"))) {
		TypeCompilerTokenProcessor(
			TypeCompiler(
				FunctionCompilerTypeParent(this),
				module.lines,
				type()))
	}!!

fun FunctionItCompiler.plus(token: Token): TokenProcessor =
	notNullIf(token == token(begin("does"))) {
		BodyCompilerTokenProcessor(
			BodyCompiler(
				BodyCompiler.Parent.FunctionItDoes(this),
				module.begin(type.given).body(compiled())))
	}!!

fun FunctionItDoesCompiler.plus(token: Token): TokenProcessor =
	notNullIf(token == token(end)) {
		parentOrNull!!.plus(arrowCompiled)
	}!!

fun FunctionCompiler.Parent.plus(arrowCompiled: ArrowCompiled): TokenProcessor =
	when (this) {
		is FunctionCompiler.Parent.Define ->
			DefineCompilerTokenProcessor(defineCompiler.plus(arrowCompiled.asDefinition))
		is FunctionCompiler.Parent.Body ->
			BodyCompilerTokenProcessor(bodyCompiler.plus(lineCompiled(arrowCompiled)))
	}

fun FunctionCompiler.plus(type: Type): TokenProcessor =
	FunctionItCompiler(parentOrNull, module, type).asTokenProcessor

fun FunctionItCompiler.plus(compiled: Compiled): TokenProcessor =
	FunctionItDoesCompiler(parentOrNull, fn(compiled.term).of(type arrowTo compiled.type))
		.asTokenProcessor
