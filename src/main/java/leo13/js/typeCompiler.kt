package leo13.js

data class TypeCompiler(
	val type: Type,
	val ret: (Type) -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is BeginToken ->
				when (token.begin.string) {
					"number" -> EmptyCompiler { copy(type = type(numberLine)) }
					"string" -> EmptyCompiler { copy(type = type(stringLine)) }
					else -> TypeCompiler(emptyType) { rhs ->
						copy(type = type.plus(line(token.begin.string fieldTo rhs)))
					}
				}
			is EndToken -> ret(type)
			else -> TODO()
		}
}