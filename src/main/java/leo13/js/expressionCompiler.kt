package leo13.js

import leo13.stack

data class ExpressionCompiler(
	val typed: Typed,
	val ret: Typed.() -> Compiler) : Compiler {
	override fun write(token: Token) =
		when (token) {
			is DoubleToken ->
				copy(typed = typed.expression then expression(token.double) of doubleType)
			is StringToken ->
				copy(typed = typed.expression then expression(token.string) of stringType)
			is BeginToken -> when (token.begin.string) {
				"native" -> StringCompiler(null) {
					copy(typed = typed.expression then expression(native(this)) of nativeType)
				}
				"do" -> ExpressionCompiler(nullTyped) {
					copy(typed = typed.expression then expression of type)
				}
				"call" ->
					if (typed.type == nativeType)
						CallCompiler(typed.expression, null, stack()) {
							this@ExpressionCompiler.copy(typed = expression(this) of nativeType)
						}
					else error("native lhs expected")
				else -> TODO()
			}
			is EndToken -> typed.ret()
		}
}

fun compile(fn: Compiler.() -> Compiler): Typed =
	(fn(ExpressionCompiler(nullTyped) {
		EofCompiler(this)
	}).write(token(end)) as EofCompiler).typed