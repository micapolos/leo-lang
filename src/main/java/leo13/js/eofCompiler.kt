package leo13.js

data class EofCompiler(val typed: Typed) : Compiler {
	override fun write(token: Token) = error("eof")
}