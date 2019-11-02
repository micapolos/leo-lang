package leo13.js.compiler

data class TypesCompiler(
	val types: Types,
	val ret: (Types) -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is BeginToken ->
				when (token.begin.string) {
					"number" -> EmptyCompiler { copy(types = types(numberType)) }
					"string" -> EmptyCompiler { copy(types = types(stringType)) }
					else -> TypesCompiler(emptyTypes) { rhs ->
						copy(types = types.plus(type(token.begin.string fieldTo rhs)))
					}
				}
			is EndToken -> ret(types)
			else -> TODO()
		}
}

fun types(ret: (Types) -> Compiler) = TypesCompiler(emptyTypes, ret)