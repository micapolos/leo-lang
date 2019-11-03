package leo13.lambda

import leo13.Index
import leo13.index0
import leo13.js.compiler.*
import leo13.next

fun <T> valueCompiler(nativeCompile: Compile<T>, ret: (Value<T>) -> Compiler): Compiler =
	switchCompiler(
		choice("native", nativeCompile { native ->
			endCompiler {
				value(native).plusCompiler(nativeCompile, ret)
			}
		}),
		choice("function", recursive {
			valueCompiler(nativeCompile) { body ->
				value(abstraction(body)).plusCompiler(nativeCompile, ret)
			}
		}),
		choice("variable",
			indexCompiler { index ->
				value(variable<T>(index)).plusCompiler(nativeCompile, ret)
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

fun <T> Value<T>.plusCompiler(nativeCompile: Compile<T>, ret: (Value<T>) -> Compiler): Compiler =
	compiler { token ->
		when (token) {
			is BeginToken ->
				if (token.begin.string == "apply") valueCompiler(nativeCompile) {
					invoke(it).plusCompiler(nativeCompile, ret)
				}
				else error("$token not expected")
			is EndToken -> ret(this)
			else -> error("$token not expected")
		}
	}