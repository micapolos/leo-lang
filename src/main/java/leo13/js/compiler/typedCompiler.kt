package leo13.js.compiler

data class TypedCompiler(
	val typed: Typed,
	val ret: (Typed) -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is BeginToken -> when (token.begin.string) {
				else -> typed { rhs ->
					copy(typed = typed.plus(token.begin.string, rhs))
				}
			}
			is EndToken -> ret(typed)
			else -> TODO()
		}
}

fun typed(ret: (Typed) -> Compiler) = TypedCompiler(emptyTyped, ret)

fun compile(fn: Compiler.() -> Compiler): Typed =
	(fn(typed { typed ->
		EofCompiler(typed)
	}).write(token(end)) as EofCompiler).typed