package leo14.lambda

import leo13.Index
import leo13.index0
import leo13.next
import leo14.*
import leo14.js.compiler.choice
import leo14.js.compiler.fallback

fun <T> compileTerm(fallbackCompile: Compile<Term<T>>, nativeCompile: Compile<T>): Compile<Term<T>> =
	{ ret -> termCompiler(fallbackCompile, nativeCompile, ret) }

fun <T> termCompiler(fallbackCompile: Compile<Term<T>>, nativeCompile: Compile<T>, ret: Ret<Term<T>>): Compiler =
	compileTerm(fallbackCompile, nativeCompile).let { compileTerm ->
		switchCompiler(
			fallback(fallbackCompile { fallback ->
				ret(fallback)
			}),
			choice("native", nativeCompile { native ->
				endCompiler {
					term(native).plusCompiler(compileTerm, ret)
				}
			}),
			choice("function", recursive {
				termCompiler(fallbackCompile, nativeCompile) { body ->
					term(abstraction(body)).plusCompiler(compileTerm, ret)
				}
			}),
			choice("argument",
				indexCompiler { index ->
					term(variable<T>(index)).plusCompiler(compileTerm, ret)
				}))
	}

fun indexCompiler(ret: (Index) -> Compiler): Compiler =
	index0.plusCompiler(ret)

fun Index.plusCompiler(ret: (Index) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is EndToken -> ret(this)
			is BeginToken -> next.plusCompiler { index ->
				endCompiler {
					ret(index)
				}
			}
			else -> error("not an index")
		}
	}

fun <T> Term<T>.plusCompiler(compileTerm: Compile<Term<T>>, ret: Ret<Term<T>>): Compiler =
	compiler { token ->
		when (token) {
			is BeginToken ->
				if (token.begin.string == "apply") compileTerm { term ->
					invoke(term).plusCompiler(compileTerm, ret)
				}
				else error("$token not expected")
			is EndToken -> ret(this)
			else -> error("$token not expected")
		}
	}
