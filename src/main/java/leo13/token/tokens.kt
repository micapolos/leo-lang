package leo13.token

import leo13.*
import leo13.script.asScript

data class Tokens(val stack: Stack<Token>) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = tokensName
	override val scriptableBody get() = stack.asScript { scriptableLine }
}

val Stack<Token>.tokens get() = Tokens(this)
fun Tokens.plus(token: Token) = stack.push(token).tokens
fun tokens(vararg tokens: Token) = Tokens(stack(*tokens))
val String.unsafeTokens get() = tokenizer().push(this).completedTokensOrThrow