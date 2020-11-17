package leo21.token.body

import leo.base.notNullIf
import leo14.Token
import leo14.begin
import leo14.end
import leo14.lambda.fn
import leo14.orError
import leo14.token
import leo15.dsl.*
import leo21.compiled.ArrowCompiled
import leo21.compiled.Compiled
import leo21.compiled.compiled
import leo21.compiled.lineCompiled
import leo21.compiled.of
import leo21.token.processor.BodyCompilerProcessor
import leo21.token.processor.DefineCompilerProcessor
import leo21.token.processor.Processor
import leo21.token.processor.TypeCompilerProcessor
import leo21.token.processor.processor
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

fun FunctionCompiler.plus(token: Token): Processor =
	notNullIf(token == token(begin("it"))) {
		TypeCompilerProcessor(
			TypeCompiler(
				FunctionCompilerTypeParent(this),
				module.lines,
				type()))
	}.orError { expected { word { it } } }

fun FunctionItCompiler.plus(token: Token): Processor =
	notNullIf(token == token(begin("does"))) {
		BodyCompilerProcessor(
			BodyCompiler(
				BodyCompiler.Parent.FunctionItDoes(this),
				module.begin(type.given).body(compiled())))
	}.orError { expected { word { does } } }

fun FunctionItDoesCompiler.plus(token: Token): Processor =
	notNullIf(token == token(end)) {
		parentOrNull!!.plus(arrowCompiled)
	}.orError { expected { end } }

fun FunctionCompiler.Parent.plus(arrowCompiled: ArrowCompiled): Processor =
	when (this) {
		is FunctionCompiler.Parent.Define ->
			DefineCompilerProcessor(defineCompiler.plus(arrowCompiled.asDefinition))
		is FunctionCompiler.Parent.Body ->
			BodyCompilerProcessor(bodyCompiler.plus(lineCompiled(arrowCompiled)))
	}

fun FunctionCompiler.plus(type: Type): Processor =
	FunctionItCompiler(parentOrNull, module, type).processor

fun FunctionItCompiler.plus(compiled: Compiled): Processor =
	FunctionItDoesCompiler(parentOrNull, fn(compiled.term).of(type arrowTo compiled.type))
		.processor
