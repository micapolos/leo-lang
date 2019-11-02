package leo13.js.compiler

import leo13.Stack
import leo13.push

data class CallCompiler(
	val lhs: Expression,
	val nameOrNull: String?,
	val argStack: Stack<Expression>,
	val ret: (Call) -> Compiler) : Compiler {
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
							TypedCompiler(functions(), nullTyped) { typed ->
								CallCompiler(lhs, nameOrNull, argStack.push(typed.expression), ret)
							}
						else -> error("with expected")
					}
				is EndToken ->
					ret(lhs.call(nameOrNull, argStack))
				else -> error("with or end expected")
			}
}