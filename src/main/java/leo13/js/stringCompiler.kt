package leo13.js

data class StringCompiler(val stringOrNull: String?, val ret: String.() -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is StringToken ->
				if (stringOrNull == null) copy(token.string)
				else error("string expected")
			is EndToken ->
				if (stringOrNull != null) stringOrNull.ret()
				else error("string expected")
			else -> error("string expected")
		}
}