package leo13.js

data class TypeCompiler(
	val type: Type,
	val ret: Type.() -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is EndToken -> type.ret()
			else -> TODO()
		}
}