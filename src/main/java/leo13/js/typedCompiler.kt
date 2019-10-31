package leo13.js

import leo13.stack

data class TypedCompiler(
	val typed: Typed,
	val ret: (Typed) -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is NumberToken ->
				copy(typed = typed.expression then expression(token.number) of types(numberType))
			is StringToken ->
				copy(typed = typed.expression then expression(token.string) of types(stringType))
			is BeginToken -> when (token.begin.string) {
				"native" -> StringCompiler(null) {
					copy(typed = typed.expression then expression(native(this)) of types(nativeType))
				}
				"do" -> TypedCompiler(nullTyped) { rhs ->
					copy(typed = typed.expression then rhs.expression of rhs.types)
				}
				"call" ->
					if (typed.types == types(nativeType))
						CallCompiler(typed.expression, null, stack()) { call ->
							copy(typed = expression(call) of types(nativeType))
						}
					else error("native lhs expected")
				else -> TODO()
			}
			is EndToken -> ret(typed)
		}
}

fun compile(fn: Compiler.() -> Compiler): Typed =
	(fn(TypedCompiler(nullTyped) { typed ->
		EofCompiler(typed)
	}).write(token(end)) as EofCompiler).typed