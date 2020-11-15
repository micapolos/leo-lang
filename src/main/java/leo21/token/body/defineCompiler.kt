package leo21.token.body

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo21.token.processor.FunctionCompilerTokenProcessor
import leo21.token.processor.TokenProcessor
import leo21.token.processor.asTokenProcessor

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
			else -> null
		}
		is EndToken -> parentOrNull!!.plus(module.definitions)
	}!!

fun DefineCompiler.plus(definition: Definition): DefineCompiler =
	copy(module = module.plus(definition))

fun DefineCompiler.Parent.plus(definitions: Definitions): TokenProcessor =
	when (this) {
		is DefineCompiler.Parent.Body -> bodyCompiler.plus(definitions).asTokenProcessor
	}