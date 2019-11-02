package leo13.js.compiler

data class EndCompiler(val ret: () -> Compiler) : Compiler {
	override fun write(token: Token) =
		if (token is EndToken) ret()
		else error("end expected")
}

fun end(ret: () -> Compiler) = EndCompiler(ret)