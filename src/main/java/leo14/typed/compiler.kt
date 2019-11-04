package leo14.typed

import leo14.*

fun <T> compiledCompiler(compiled: Compiled<T>, ret: Ret<Compiled<T>>): Compiler =
	compiler { token ->
		when (token) {
			is LiteralToken -> compiled.compilerNative(token.literal) { typed ->
				compiledCompiler(compiled.context.with(typed), ret)
			}
			is BeginToken -> compiled.compiler(token.begin.string, ret)
			is EndToken -> ret(compiled)
		}
	}