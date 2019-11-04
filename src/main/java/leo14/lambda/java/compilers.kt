package leo14.lambda.java

import leo14.*
import leo14.lambda.termCompiler

fun nativeCompiler(ret: (Native) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is LiteralToken -> ret(native(token.literal))
			else -> error("$token is not native java")
		}
	}

val compileNative: Compile<Native> = { nativeCompiler(it) }

val compiler
	get() =
		termCompiler(
			compileError("fallback"),
			compileNative,
			ret())
