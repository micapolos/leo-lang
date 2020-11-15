package leo21.token.body

import leo13.onlyOrNull
import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.token.processor.DefineCompilerTokenProcessor
import leo21.token.processor.FunctionCompilerTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.token.processor.TypeCompilerTokenProcessor
import leo21.token.processor.processor
import leo21.token.type.compiler.DefineCompilerTypeParent
import leo21.token.type.compiler.TypeCompiler
import leo21.type.Line
import leo21.type.Type
import leo21.type.struct
import leo21.type.type

data class DefineCompiler(
	val parentOrNull: Parent?,
	val module: Module
) {

	sealed class Parent {
		data class Body(val bodyCompiler: BodyCompiler) : Parent()
	}
}

fun DefineCompiler.plus(token: Token): TokenProcessor =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> when (token.begin.string) {
			"function" ->
				FunctionCompilerTokenProcessor(
					FunctionCompiler(
						FunctionCompiler.Parent.Define(this),
						module))
			"type" ->
				TypeCompilerTokenProcessor(
					TypeCompiler(
						DefineCompilerTypeParent(this),
						module.lines,
						type()))
			else -> null
		}
		is EndToken -> parentOrNull!!.plus(module.definitions)
	}!!

fun DefineCompiler.plus(definition: Definition): DefineCompiler =
	copy(module = module.plus(definition))

fun DefineCompiler.plus(line: Line): DefineCompiler =
	copy(module = module.plus(line))

fun DefineCompiler.plus(type: Type): TokenProcessor =
	DefineCompilerTokenProcessor(plus(type.struct.lineStack.onlyOrNull!!))

fun DefineCompiler.Parent.plus(definitions: Definitions): TokenProcessor =
	when (this) {
		is DefineCompiler.Parent.Body -> bodyCompiler.plus(definitions).processor
	}
