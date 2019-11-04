package leo14.lambda

import leo13.Index
import leo13.index0
import leo13.js.compiler.choice
import leo13.js.compiler.fallback
import leo13.next
import leo14.*

fun <T> termCompiler(fallbackCompile: Compile<Term<T>>, nativeCompile: Compile<T>, ret: (Term<T>) -> Compiler): Compiler =
	switchCompiler(
		fallback(fallbackCompile { fallback ->
			ret(fallback)
		}),
		choice("native", nativeCompile { native ->
			endCompiler {
				term(native).plusCompiler(fallbackCompile, nativeCompile, ret)
			}
		}),
		choice("function", recursive {
			termCompiler(fallbackCompile, nativeCompile) { body ->
				term(abstraction(body)).plusCompiler(fallbackCompile, nativeCompile, ret)
			}
		}),
		choice("argument",
			indexCompiler { index ->
				term(variable<T>(index)).plusCompiler(fallbackCompile, nativeCompile, ret)
			}))

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

fun <T> Term<T>.plusCompiler(fallbackCompile: Compile<Term<T>>, nativeCompile: Compile<T>, ret: (Term<T>) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is BeginToken ->
				if (token.begin.string == "apply") termCompiler(fallbackCompile, nativeCompile) {
					invoke(it).plusCompiler(fallbackCompile, nativeCompile, ret)
				}
				else error("$token not expected")
			is EndToken -> ret(this)
			else -> error("$token not expected")
		}
	}

// compiler2 - number and strings becomes terms

fun <T> termCompiler2(fallbackCompile: Compile<Term<T>>, nativeCompile: Compile<T>, ret: (Term<T>) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is NumberToken -> ret(term(token.number.roundInt))
			is StringToken -> ret(stringTerm(token.string))
			is BeginToken ->
				when (token.begin.string) {
					"native" -> nativeCompile { native ->
						endCompiler {
							term(native).plusCompiler(fallbackCompile, nativeCompile, ret)
						}
					}
					"function" -> termCompiler2(fallbackCompile, nativeCompile) { body ->
						term(abstraction(body)).plusCompiler(fallbackCompile, nativeCompile, ret)
					}
					"argument" -> indexCompiler { index ->
						term(variable<T>(index)).plusCompiler(fallbackCompile, nativeCompile, ret)
					}
					else -> fallbackCompile { fallback ->
						ret(fallback)
					}
				}
			is EndToken -> error("empty term") // TODO: Should we return null term, like: identity?
		}
	}


