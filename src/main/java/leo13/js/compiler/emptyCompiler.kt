package leo13.js.compiler

data class EmptyCompiler(var ret: () -> Compiler) : Compiler {
	override fun write(token: Token) =
		if (token is EndToken) ret()
		else error("empty expected")
}