package leo13.js

data class TypeCompiler(
	val type: Type,
	val ret: (Type) -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is BeginToken ->
				when (token.begin.string) {
					"double" -> EmptyCompiler { copy(type = doubleType) }
					"string" -> EmptyCompiler { copy(type = stringType) }
					"null" -> EmptyCompiler { copy(type = nullType) }
					else -> TypeCompiler(emptyType) { rhs ->
						copy(type = type.plus(token.begin.string fieldTo rhs))
					}
				}
			is EndToken -> ret(type)
			else -> TODO()
		}
}