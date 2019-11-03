package leo13.lambda

import leo13.Index
import leo13.index0
import leo13.js.compiler.*
import leo13.next

fun <T> valueCompiler(fallbackCompile: Compile<Value<T>>, nativeCompile: Compile<T>, ret: (Value<T>) -> Compiler): Compiler =
	switchCompiler(
		fallback(fallbackCompile { fallback ->
			ret(fallback)
		}),
		choice("native", nativeCompile { native ->
			endCompiler {
				value(native).plusCompiler(fallbackCompile, nativeCompile, ret)
			}
		}),
		choice("function", recursive {
			valueCompiler(fallbackCompile, nativeCompile) { body ->
				value(abstraction(body)).plusCompiler(fallbackCompile, nativeCompile, ret)
			}
		}),
		choice("argument",
			indexCompiler { index ->
				value(variable<T>(index)).plusCompiler(fallbackCompile, nativeCompile, ret)
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

fun <T> Value<T>.plusCompiler(fallbackCompile: Compile<Value<T>>, nativeCompile: Compile<T>, ret: (Value<T>) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is BeginToken ->
				if (token.begin.string == "apply") valueCompiler(fallbackCompile, nativeCompile) {
					invoke(it).plusCompiler(fallbackCompile, nativeCompile, ret)
				}
				else error("$token not expected")
			is EndToken -> ret(this)
			else -> error("$token not expected")
		}
	}
