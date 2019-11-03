package leo14.lambda.eval

import leo14.*
import leo14.lambda.valueCompiler

fun anyCompiler(ret: (Any) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is StringToken -> ret(token.string)
			is NumberToken -> ret(token.number.any)
			else -> error("$token is not native java")
		}
	}

val compileAny: Compile<Any> = { anyCompiler(it) }

val compiler
	get() =
		valueCompiler(
			compileError("fallback"),
			compileAny,
			ret())
