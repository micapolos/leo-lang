package leo13.js.compiler

data class EofCompiler(val typed: Typed) : Compiler {
	override fun write(token: Token) = error("eof")
}