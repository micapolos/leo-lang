package leo13.js

data class FunctionsCompiler(
	val functions: Functions,
	val ret: (Functions) -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is BeginToken ->
				if (token.begin.string == "function")
					function(functions) { function ->
						copy(functions = functions.plus(function))
					}
				else error("function expected")
			is EndToken -> ret(functions)
			else -> error("dupa")
		}
}

fun functions(ret: (Functions) -> Compiler): Compiler =
	FunctionsCompiler(functions(), ret)