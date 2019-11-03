package leo13.js.compiler

data class EndCompiler(val ret: () -> Compiler) : Compiler {
	override fun write(token: Token) =
		if (token is EndToken) ret()
		else error("end expected")
}

fun endCompiler(ret: () -> Compiler) = EndCompiler(ret)

fun <T> end(value: T): Compile<T> =
	{ ret ->
		endCompiler {
			ret(value)
		}
	}
