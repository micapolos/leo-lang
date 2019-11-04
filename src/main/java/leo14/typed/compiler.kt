package leo14.typed

import leo14.*

fun <T> compiledCompiler(compiled: Compiled<T>, ret: Ret<Compiled<T>>): Compiler =
	compiler { token ->
		when (token) {
			is NumberToken -> compiled.compilerNative(token.number, ret)
			is StringToken -> compiled.compilerNative(token.string, ret)
			is BeginToken -> compiled.compiler(token.begin.string, ret)
			is EndToken -> ret(compiled)
		}
	}