package leo13.js

import leo13.Stack
import leo13.push

data class CallCompiler(
	val lhs: Expression,
	val nameOrNull: String?,
	val argStack: Stack<Expression>,
	val ret: Call.() -> Compiler) : Compiler {
	override fun write(token: Token) =
		if (nameOrNull == null)
			when (token) {
				is StringToken -> CallCompiler(lhs, token.string, argStack, ret)
				else -> error("call string expected")
			}
		else
			when (token) {
				is BeginToken ->
					when (token.begin.string) {
						"with" ->
							ExpressionCompiler(nullTyped) {
								CallCompiler(lhs, nameOrNull, argStack.push(expression), ret)
							}
						else -> error("with expected")
					}
				is EndToken ->
					lhs.call(nameOrNull, argStack).ret()
				else ->
					if (nameOrNull == null) error("name expected")
					else error("with expected")
			}
}