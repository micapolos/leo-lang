package leo13.js

interface Compiler {
	fun write(token: Token): Compiler
}

val eofCompiler = object : Compiler {
	override fun write(token: Token) = error("eof")
}