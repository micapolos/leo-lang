package leo13.js.compiler

data class BeginCompiler(
	val string: String,
	val ret: () -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is BeginToken ->
				if (token.begin.string == string) ret()
				else error("expected $string")
			else -> error("not expected")
		}
}

fun beginCompiler(string: String, ret: () -> Compiler) = BeginCompiler(string, ret)

fun <T> begin(string: String, then: Compile<T>): Compile<T> =
	{ ret ->
		beginCompiler(string) {
			then(ret)
		}
	}
