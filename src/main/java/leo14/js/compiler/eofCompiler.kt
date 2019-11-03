package leo13.js.compiler

import leo14.Compiler
import leo14.Token

data class EofCompiler(val typed: Typed) : Compiler {
	override fun write(token: Token) = error("eof")
}