package leo14.lambda.scheme

import leo.base.string
import leo14.*
import leo14.lambda.termCompiler

fun codeCompiler(ret: (Code) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is LiteralToken -> ret(code(token.literal.string))
			else -> error("$token is not native scheme")
		}
	}

val compileNative: Compile<Code> = { codeCompiler(it) }

val compiler
	get() =
		termCompiler(
			compileError("fallback"),
			compileNative,
			ret())
