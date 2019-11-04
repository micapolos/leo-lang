package leo14.lambda.eval

import leo14.*
import leo14.lambda.termCompiler

fun anyCompiler(ret: (Any) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is LiteralToken -> ret(token.literal.any)
			else -> error("$token is not native java")
		}
	}

val compileAny: Compile<Any> = { anyCompiler(it) }

val compiler
	get() =
		termCompiler(
			compileError("fallback"),
			compileAny,
			ret())
