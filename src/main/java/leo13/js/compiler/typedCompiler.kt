package leo13.js.compiler

import leo13.lambda.js.value

data class TypedCompiler(
	val typed: Typed,
	val ret: (Typed) -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is NumberToken ->
				if (typed.types != emptyTypes) error("number not expected")
				else copy(typed = token.number.value of types(numberType))
			is StringToken ->
				if (typed.types != emptyTypes) error("string not expected")
				else copy(typed = value(token.string) of types(stringType))
			is BeginToken -> when (token.begin.string) {
				else -> typed { rhs ->
					copy(typed = typed.plus(token.begin.string, rhs))
				}
			}
			is EndToken -> ret(typed)
		}
}

fun typed(ret: (Typed) -> Compiler) = TypedCompiler(nullTyped, ret)

fun compile(fn: Compiler.() -> Compiler): Typed =
	(fn(TypedCompiler(nullTyped) { typed ->
		EofCompiler(typed)
	}).write(token(end)) as EofCompiler).typed