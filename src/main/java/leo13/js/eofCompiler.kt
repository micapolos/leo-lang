package leo13.js

data class EofCompiler(val jsWriter: Writer) : Compiler {
	override fun write(token: Token) = error("eof")
}